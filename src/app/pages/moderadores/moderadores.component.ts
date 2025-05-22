import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ModeradoresService, Moderador } from './moderadores.service';
import { FormsModule } from '@angular/forms';
import * as ExcelJS from 'exceljs';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-moderadores',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './moderadores.component.html',
  styleUrls: ['./moderadores.component.css']
})
export class ModeradoresComponent implements OnInit {
  moderadores: Moderador[] = [];
  moderadorSeleccionado: Moderador | null = null;

  terminoBusqueda: string = '';
  filtroEstadoId: number | undefined;
  estados: { id_estado: number; nombre: string }[] = [];

  paginaActual: number = 1;
  tamanoPagina: number = 9;

  mensajeAlerta: string | null = null;
  tipoAlerta: 'success' | 'danger' = 'success';

  nuevoModerador: Partial<Moderador> = {
    nombre: '',
    email: '',
    id_estado: undefined
  };

  formatoExportacion: 'excel' | 'sql' = 'excel';



  constructor(private moderadoresService: ModeradoresService) { }

  ngOnInit(): void {
    this.cargarModeradores();
    this.cargarEstados();
  }

  mostrarAlerta(mensaje: string, tipo: 'success' | 'danger'): void {
    this.mensajeAlerta = mensaje;
    this.tipoAlerta = tipo;
    setTimeout(() => this.mensajeAlerta = null, 4000);
  }

  cerrarAlerta(): void {
    this.mensajeAlerta = null;
  }

  limpiarFormulario(): void {
    this.nuevoModerador = {
      nombre: '',
      email: '',
      id_estado: undefined
    };
  }

  cargarModeradores(): void {
    this.moderadoresService.obtenerModeradores().subscribe({
      next: (data) => {
        this.moderadores = data.map(m => ({ ...m, seleccionado: false }));
      },
      error: (err) => {
        console.error('❌ Error al obtener moderadores:', err);
        this.mostrarAlerta('❌ Error al cargar moderadores', 'danger');
      }
    });
  }

  cargarEstados(): void {
    this.moderadoresService.obtenerEstados().subscribe({
      next: (data) => {
        this.estados = data.filter(e => e.tipo === 'moderador');
      },
      error: (err) => {
        console.error('❌ Error al obtener estados:', err);
      }
    });
  }

  seleccionarModerador(moderador: Moderador): void {
    this.moderadorSeleccionado = moderador;
  }

  cargarModeradoresConSeleccion(id?: number): void {
    this.moderadoresService.obtenerModeradores().subscribe({
      next: (data) => {
        this.moderadores = data.map(m => ({ ...m, seleccionado: false }));
        if (id) {
          const reencontrado = this.moderadores.find(m => m.id_moderador === id);
          if (reencontrado) {
            this.seleccionarModerador(reencontrado);
          }
        }
      },
      error: (err) => {
        console.error('❌ Error al obtener moderadores:', err);
        this.mostrarAlerta('❌ Error al cargar moderadores', 'danger');
      }
    });
  }


  limpiarSeleccion(): void {
    this.moderadorSeleccionado = null;
    this.limpiarFormulario();
  }

  guardarModerador(): void {
    const { id_moderador, nombre, email, id_estado } = this.nuevoModerador;

    if (!nombre?.trim() || !email?.trim() || id_estado === undefined) {
      this.mostrarAlerta('❗ Todos los campos son obligatorios.', 'danger');
      return;
    }

    const callback = () => {
      this.mostrarAlerta(
        id_moderador ? '✅ Moderador actualizado correctamente' : '✅ Moderador guardado correctamente',
        'success'
      );
      this.cerrarModal('modalAgregarModerador');
      this.limpiarFormulario();
      this.cargarModeradoresConSeleccion(id_moderador ?? undefined);

    };

    const errorCallback = () => {
      this.mostrarAlerta(
        id_moderador ? '❌ Error al actualizar el moderador' : '❌ Error al guardar el moderador',
        'danger'
      );
    };

    if (id_moderador) {
      this.moderadoresService.editarModerador(id_moderador, { nombre, email, id_estado }).subscribe({
        next: callback,
        error: errorCallback
      });
    } else {
      this.moderadoresService.agregarModerador({ nombre, email, id_estado }).subscribe({
        next: callback,
        error: errorCallback
      });
    }
  }


  get moderadoresFiltrados(): Moderador[] {
    return this.moderadores.filter(m => {
      const coincideNombre = m.nombre.toLowerCase().includes(this.terminoBusqueda.toLowerCase());
      const coincideEstado = this.filtroEstadoId !== undefined ? m.id_estado === this.filtroEstadoId : true;
      return coincideNombre && coincideEstado;
    });
  }

  get totalPaginas(): number {
    return Math.ceil(this.moderadoresFiltrados.length / this.tamanoPagina) || 1;
  }

  get moderadoresPaginados(): Moderador[] {
    const inicio = (this.paginaActual - 1) * this.tamanoPagina;
    return this.moderadoresFiltrados.slice(inicio, inicio + this.tamanoPagina);
  }

  irPrimeraPagina(): void { this.paginaActual = 1; }
  irUltimaPagina(): void { this.paginaActual = this.totalPaginas; }
  irPaginaAnterior(): void { if (this.paginaActual > 1) this.paginaActual--; }
  irPaginaSiguiente(): void { if (this.paginaActual < this.totalPaginas) this.paginaActual++; }

  estanTodosSeleccionados(): boolean {
    return this.moderadoresFiltrados.length > 0 &&
      this.moderadoresFiltrados.every(m => m.seleccionado);
  }

  cantidadSeleccionados(): number {
    return this.obtenerModeradoresSeleccionados().length;
  }

  onSeleccionarTodosChange(event: any): void {
    const checked = event.target.checked;
    this.moderadoresFiltrados.forEach(m => m.seleccionado = checked);
  }

  obtenerModeradoresSeleccionados(): Moderador[] {
    return this.moderadores.filter(m => m.seleccionado);
  }

  hayModeradoresSeleccionados(): boolean {
    return this.obtenerModeradoresSeleccionados().length > 0;
  }

  abrirModalEliminarSeleccionados(): void {
    const seleccionados = this.obtenerModeradoresSeleccionados();

    if (seleccionados.length === 0) {
      this.mostrarAlerta('⚠️ No hay moderadores seleccionados.', 'danger');
      return;
    }

    const modalEl = document.getElementById('modalConfirmarEliminacionModeradores');
    if (modalEl) {
      const Modal = (window as any).bootstrap.Modal;
      const modalInstance = new Modal(modalEl);
      modalInstance.show();
    }
  }

  confirmarEliminarSeleccionados(): void {
    const seleccionados = this.obtenerModeradoresSeleccionados();
    const ids = seleccionados.map(m => m.id_moderador);

    if (ids.length === 0) {
      this.mostrarAlerta('⚠️ No hay moderadores seleccionados.', 'danger');
      return;
    }

    this.moderadoresService.eliminarModeradores(ids).subscribe({
      next: () => {
        this.mostrarAlerta('✅ Moderadores eliminados correctamente.', 'success');
        this.cargarModeradores();
        this.limpiarSeleccion();
        this.cerrarModal('modalConfirmarEliminacionModeradores');
      },
      error: () => this.mostrarAlerta('❌ Error al eliminar moderadores.', 'danger')
    });
  }

  abrirModalExportarModeradores(): void {
    const modal = document.getElementById('modalExportarModeradores');
    if (modal) {
      const bsModal = new (window as any).bootstrap.Modal(modal);
      bsModal.show();
    }
  }

  async confirmarExportacionModeradores(): Promise<void> {
    const seleccionados = this.obtenerModeradoresSeleccionados();
    const data = seleccionados.length > 0 ? seleccionados : this.moderadores;

    if (this.formatoExportacion === 'excel') {
      await this.exportarModeradoresAExcel(data);
    } else {
      this.exportarModeradoresACSV(data);
    }

    this.cerrarModal('modalExportarModeradores');
  }

  async exportarModeradoresAExcel(data: Moderador[]): Promise<void> {
    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet('Moderadores');

    worksheet.columns = [
      { header: 'ID', key: 'id_moderador', width: 10 },
      { header: 'Nombre', key: 'nombre', width: 30 },
      { header: 'Email', key: 'email', width: 30 },
      { header: 'Estado', key: 'estado_nombre', width: 20 },
      { header: 'Fecha Alta', key: 'fecha_alta', width: 15 },
      { header: 'Fecha Baja', key: 'fecha_baja', width: 15 }
    ];

    data.forEach(m => worksheet.addRow(m));

    const buffer = await workbook.xlsx.writeBuffer();
    const blob = new Blob([buffer], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    });
    FileSaver.saveAs(blob, 'moderadores.xlsx');
  }

  exportarModeradoresACSV(data: Moderador[]): void {
    const encabezados = ['ID', 'Nombre', 'Email', 'Estado', 'Fecha Alta', 'Fecha Baja'];
    const filas = data.map(m =>
      [
        m.id_moderador,
        `"${m.nombre}"`,
        `"${m.email}"`,
        `"${m.estado_nombre}"`,
        m.fecha_alta,
        m.fecha_baja
      ].join(',')
    );

    const contenido = [encabezados.join(','), ...filas].join('\n');
    const blob = new Blob([contenido], { type: 'text/csv;charset=utf-8;' });
    FileSaver.saveAs(blob, 'moderadores.csv');
  }

  cerrarModal(modalId: string): void {
    const modalEl = document.getElementById(modalId);
    if (modalEl) {
      const Modal = (window as any).bootstrap.Modal;
      let instance = Modal.getInstance(modalEl);
      if (!instance) {
        instance = new Modal(modalEl);
      }
      instance.hide();
    }
  }

  cargarParaEditar(): void {
    if (!this.moderadorSeleccionado) return;

    this.nuevoModerador = {
      id_moderador: this.moderadorSeleccionado.id_moderador,
      nombre: this.moderadorSeleccionado.nombre,
      email: this.moderadorSeleccionado.email,
      id_estado: this.moderadorSeleccionado.id_estado
    };
  }

  darDeAlta(): void {
    if (!this.moderadorSeleccionado) return;

    const id = this.moderadorSeleccionado.id_moderador;
    this.moderadoresService.darDeAlta(id).subscribe({
      next: () => {
        this.mostrarAlerta('✅ Moderador dado de alta correctamente', 'success');
        this.cargarModeradores();
      },
      error: () => this.mostrarAlerta('❌ Error al dar de alta al moderador', 'danger')
    });
  }

  darDeBaja(): void {
    if (!this.moderadorSeleccionado) return;

    const id = this.moderadorSeleccionado.id_moderador;
    this.moderadoresService.darDeBaja(id).subscribe({
      next: () => {
        this.mostrarAlerta('✅ Moderador dado de baja correctamente', 'success');
        this.cargarModeradores();
      },
      error: () => this.mostrarAlerta('❌ Error al dar de baja al moderador', 'danger')
    });
  }
  readmitir(): void {
    if (!this.moderadorSeleccionado) return;

    const id = this.moderadorSeleccionado.id_moderador;
    this.moderadoresService.readmitir(id).subscribe({
      next: () => {
        this.mostrarAlerta('✅ Moderador readmitido correctamente', 'success');
        this.cargarModeradores();
      },
      error: () => this.mostrarAlerta('❌ Error al readmitir al moderador', 'danger')
    });
  }



  formatearFecha(fecha: string | null): string {
    if (!fecha) return '-';
    const [year, month, day] = fecha.split('-');
    return `${day}-${month}-${year}`;
  }

  limpiarFiltros(): void {
    this.terminoBusqueda = '';
    this.filtroEstadoId = undefined;
    this.paginaActual = 1;
    this.moderadorSeleccionado = null;
    this.moderadores.forEach(m => m.seleccionado = false);
  }
}