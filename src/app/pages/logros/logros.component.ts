import { Component, OnInit } from '@angular/core';
import { NgIf, NgFor, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { LogrosService, Logro } from './logros.service';
import * as ExcelJS from 'exceljs';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-logros',
  standalone: true,
  imports: [NgIf, NgFor, NgClass, FormsModule, HttpClientModule],
  templateUrl: './logros.component.html',
  styleUrls: ['./logros.component.css']
})
export class LogrosComponent implements OnInit {


  logros: Logro[] = [];

  logroSeleccionado: Logro | null = null;
  nuevoLogro: Partial<Logro> = this.getNuevoLogroInicial();
  estados: any[] = [];
  consolas: any[] = [];

  terminoBusqueda = '';
  filtroEstado: number | null = null;
  filtroConsola: number | null = null;
  paginaActual = 1;
  elementosPorPagina = 10;
  formatoExportacion: 'excel' | 'sql' = 'excel';
  cantidadExportar = 0;

  juegos: any[] = [];
  dificultades: any[] = [];
  mostrarAlertaBox = false;
  mensajeAlerta = '';
  tipoAlerta: 'success' | 'danger' = 'success';
  
  


  constructor(private logrosService: LogrosService) { }

  ngOnInit(): void {
    this.cargarLogros();
    this.cargarEstados();
    this.cargarConsolas();
    this.cargarJuegos();
    this.cargarDificultades();
  }

  cargarLogros(): void {
    this.logrosService.obtenerLogros().subscribe({
      next: (data) => this.logros = data.map(l => ({ ...l, seleccionado: false })),
      error: (err) => console.error('Error cargando logros:', err)
    });
  }

  cargarEstados(): void {
    this.logrosService.obtenerEstados().subscribe({
      next: (data) => (this.estados = data),
      error: (err) => console.error('Error cargando estados:', err)
    });
  }

  cargarConsolas(): void {
    this.logrosService.obtenerConsolas().subscribe({
      next: (data) => (this.consolas = data),
      error: (err) => console.error('Error cargando consolas:', err)
    });
  }

  cargarJuegos(): void {
    this.logrosService.obtenerJuegos().subscribe({
      next: (data) => this.juegos = data,
      error: (err) => console.error('Error cargando juegos:', err)
    });
  }

  cargarDificultades(): void {
    this.logrosService.obtenerDificultades().subscribe({
      next: (data) => this.dificultades = data,
      error: (err) => console.error('Error cargando dificultades:', err)
    });
  }

  seleccionarLogro(logro: Logro): void {
    this.logroSeleccionado = logro;
  }

  limpiarSeleccion(): void {
    this.logroSeleccionado = null;
    this.logros.forEach(l => l.seleccionado = false);
  }

  toggleSeleccionarTodos(checked: boolean): void {
    this.logrosFiltrados().forEach(l => l.seleccionado = checked);
  }

  onSeleccionarTodosChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.toggleSeleccionarTodos(input.checked);
  }

  cantidadSeleccionados(): number {
    return this.logros.filter(l => l.seleccionado).length;
  }

  limpiarTodo(): void {
    this.logros = this.logrosService.limpiarSeleccionados(this.logros);
    const filtros = this.logrosService.limpiarFiltros();
    this.terminoBusqueda = filtros.terminoBusqueda!;
    this.filtroEstado = filtros.filtroEstado!;
    this.filtroConsola = filtros.filtroConsola!;
    this.paginaActual = filtros.paginaActual!;
    this.logroSeleccionado = filtros.logroSeleccionado!;
  }

  estanTodosSeleccionados(): boolean {
    const filtrados = this.logrosFiltrados();
    return filtrados.length > 0 && filtrados.every(l => l.seleccionado);
  }

  obtenerLogrosSeleccionados(): Logro[] {
    return this.logros.filter(l => l.seleccionado);
  }

  abrirModalExportar(): void {
    const seleccionados = this.obtenerLogrosSeleccionados();
    this.cantidadExportar = seleccionados.length;
    const modal = document.getElementById('modalConfirmarExportacionLogros');
    if (modal) new (window as any).bootstrap.Modal(modal).show();
  }

  async confirmarExportacion(): Promise<void> {
    const seleccionados = this.obtenerLogrosSeleccionados();
    const logrosAExportar = [...(seleccionados.length > 0 ? seleccionados : this.logros)]
      .sort((a, b) => (a.id_logro ?? 0) - (b.id_logro ?? 0));

    if (this.formatoExportacion === 'excel') {
      await this.exportarLogrosAExcel(logrosAExportar);
    } else {
      const insertStatements = logrosAExportar.map(l => `INSERT INTO logros (nombre, descripcion, horas_estimadas, anio, fecha_inicio, fecha_fin, intentos, creditos, puntuacion, fecha_registro, id_juego, id_estado, id_dificultad, id_consola) VALUES ('${l.nombre}', '${l.descripcion}', ${l.horas_estimadas}, ${l.anio}, '${l.fecha_inicio}', '${l.fecha_fin}', ${l.intentos}, ${l.creditos}, ${l.puntuacion}, '${l.fecha_registro}', ${l.id_juego}, ${l.id_estado}, ${l.id_dificultad}, ${l.id_consola});`);

      const blob = new Blob([insertStatements.join('\n\n')], { type: 'text/plain;charset=utf-8' });
      FileSaver.saveAs(blob, 'logros.sql');
    }
  }

  async exportarLogrosAExcel(logrosAExportar: Logro[]): Promise<void> {
    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet('Logros');

    worksheet.columns = [
      { header: 'ID', key: 'id_logro', width: 8 },
      { header: 'Nombre', key: 'nombre', width: 30 },
      { header: 'Descripción', key: 'descripcion', width: 50 },
      { header: 'Horas estimadas', key: 'horas_estimadas', width: 20 },
      { header: 'Año', key: 'anio', width: 10 },
      { header: 'Inicio', key: 'fecha_inicio', width: 15 },
      { header: 'Fin', key: 'fecha_fin', width: 15 },
      { header: 'Intentos', key: 'intentos', width: 12 },
      { header: 'Créditos', key: 'creditos', width: 12 },
      { header: 'Puntuación', key: 'puntuacion', width: 15 },
      { header: 'Registro', key: 'fecha_registro', width: 15 },
      { header: 'Estado', key: 'estado_nombre', width: 20 },
      { header: 'Dificultad', key: 'dificultad_nombre', width: 20 },
      { header: 'Juego', key: 'juego_nombre', width: 30 }
    ];

    logrosAExportar.forEach(l => {
      worksheet.addRow({
        id_logro: l.id_logro,
        nombre: l.nombre,
        descripcion: l.descripcion,
        horas_estimadas: l.horas_estimadas,
        anio: l.anio,
        fecha_inicio: l.fecha_inicio,
        fecha_fin: l.fecha_fin,
        intentos: l.intentos,
        creditos: l.creditos,
        puntuacion: l.puntuacion,
        fecha_registro: l.fecha_registro,
        estado_nombre: l.estado_nombre,
        dificultad_nombre: l.dificultad_nombre,
        juego_nombre: l.juego_nombre
      });
    });

    const buffer = await workbook.xlsx.writeBuffer();
    FileSaver.saveAs(new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }), 'logros.xlsx');
  }

  abrirModalAgregar(): void {
    this.nuevoLogro = this.getNuevoLogroInicial();     // Limpiar el objeto
    this.logroSeleccionado = null;                     // Asegurar que no esté seleccionado nada

    // Resetear el formulario si ya se abrió antes
    setTimeout(() => {
      const form = document.querySelector('form[name="formLogro"]') as HTMLFormElement;
      form?.reset(); // Reset visual
    }, 0);

    const modal = document.getElementById('modalAgregarLogro');
    if (modal) (window as any).bootstrap.Modal.getOrCreateInstance(modal).show();
  }



  guardarLogro(): void {
    if (this.nuevoLogro.id_logro) {
      // Si tiene ID, actualizar
      this.logrosService.actualizarLogro(this.nuevoLogro.id_logro, this.nuevoLogro).subscribe({
        next: () => {
          this.mostrarAlerta('Logro actualizado correctamente.', 'success');
          this.cargarLogros();
          this.nuevoLogro = this.getNuevoLogroInicial();
          const modal = document.getElementById('modalAgregarLogro');
          if (modal) (window as any).bootstrap.Modal.getInstance(modal)?.hide();
        },
        error: (err) => {
          console.error('❌ Error al actualizar logro:', err);
          this.mostrarAlerta('Error al actualizar el logro. Revisa la consola.', 'danger');
        }
      });
    } else {
      // Si no tiene ID, insertar
      this.logrosService.agregarLogro(this.nuevoLogro).subscribe({
        next: (respuesta) => {
          this.mostrarAlerta('Logro agregado correctamente.', 'success');
          this.cargarLogros();
          this.nuevoLogro = this.getNuevoLogroInicial();
          const modal = document.getElementById('modalAgregarLogro');
          if (modal) (window as any).bootstrap.Modal.getInstance(modal)?.hide();
        },
        error: (err) => {
          console.error('❌ Error al insertar logro:', err);
          this.mostrarAlerta('Error al guardar el logro. Revisa la consola.', 'danger');
        }
      });
    }
  }

  mostrarAlerta(mensaje: string, tipo: 'success' | 'danger') {
    this.mensajeAlerta = mensaje;
    this.tipoAlerta = tipo;
    this.mostrarAlertaBox = true;
  }



  editarLogro(logro: Logro): void {
    this.nuevoLogro = { ...logro }; // Copia todos los campos al formulario
    const modal = document.getElementById('modalAgregarLogro');
    if (modal) (window as any).bootstrap.Modal.getOrCreateInstance(modal).show();
  }

  confirmarEliminarLogro(): void {
    if (!this.logroSeleccionado) return;

    if (confirm(`¿Estás seguro de eliminar el logro "${this.logroSeleccionado.nombre}"?`)) {
      this.logrosService.eliminarLogro(this.logroSeleccionado.id_logro!).subscribe({
        next: () => {
          console.log('✅ Logro eliminado');
          this.cargarLogros();
          this.logroSeleccionado = null;
        },
        error: (err) => {
          console.error('❌ Error al eliminar logro:', err);
          alert('Error al eliminar el logro.');
        }
      });
    }
  }

  confirmarEliminarSeleccionados(): void {
    const ids = this.obtenerLogrosSeleccionados().map(l => l.id_logro!);
    if (ids.length === 0) return;

    this.logrosService.eliminarVariosLogros(ids).subscribe({
      next: () => {
        console.log('✅ Logros eliminados correctamente');
        this.cargarLogros();
        const modal = document.getElementById('modalConfirmarEliminacionMultipleLogros');
        if (modal) (window as any).bootstrap.Modal.getInstance(modal)?.hide();
      },
      error: (err) => {
        console.error('❌ Error al eliminar múltiples logros:', err);
        alert('Error al eliminar logros. Consulta la consola.');
      }
    });
  }

  logrosFiltrados(): Logro[] {
    return this.logros.filter(logro =>
      (!this.filtroEstado || logro.id_estado === this.filtroEstado) &&
      (!this.filtroConsola || logro.id_consola === this.filtroConsola) &&
      (!this.terminoBusqueda || logro.nombre.toLowerCase().includes(this.terminoBusqueda.toLowerCase()))
    );
  }

  logrosPaginados(): Logro[] {
    const logros = this.logrosFiltrados();
    const inicio = (this.paginaActual - 1) * this.elementosPorPagina;
    return logros.slice(inicio, inicio + this.elementosPorPagina);
  }

  totalPaginas(): number {
    return Math.ceil(this.logrosFiltrados().length / this.elementosPorPagina);
  }

  irPrimeraPagina(): void { this.paginaActual = 1; }
  irUltimaPagina(): void { this.paginaActual = this.totalPaginas(); }
  irPaginaAnterior(): void { if (this.paginaActual > 1) this.paginaActual--; }
  irPaginaSiguiente(): void { if (this.paginaActual < this.totalPaginas()) this.paginaActual++; }

  formatearFecha(fecha: string | Date | null | undefined): string {
    if (!fecha) return '-';
    const d = new Date(fecha);
    const dia = d.getDate().toString().padStart(2, '0');
    const mes = (d.getMonth() + 1).toString().padStart(2, '0');
    const anio = d.getFullYear();
    return `${dia}-${mes}-${anio}`;
  }

  private getNuevoLogroInicial(): Partial<Logro> {
    return {
      nombre: '',
      descripcion: '',
      horas_estimadas: 0,
      anio: new Date().getFullYear(),
      intentos: 0,
      creditos: 0,
      puntuacion: 0,
      fecha_inicio: '',
      fecha_fin: '',
      fecha_registro: '',
      id_juego: undefined,
      id_estado: undefined,
      id_dificultad: undefined,
      id_consola: undefined,
      progreso: 0
    };
  }

  calcularPorcentaje(logro: Logro): number {
    const maxPuntos = 1000; // Ajusta este valor según tu lógica real
    const puntos = logro.puntuacion ?? 0;
    return Math.min(100, Math.round((puntos / maxPuntos) * 100));
  }


}
