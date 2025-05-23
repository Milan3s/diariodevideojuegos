import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import * as ExcelJS from 'exceljs';
import * as FileSaver from 'file-saver';
import { MetasTwitchService, MetaTwitch } from './metas-twitch.service';

@Component({
  selector: 'app-metas-twitch',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './metas-twitch.component.html',
  styleUrls: ['./metas-twitch.component.css']
})
export class MetasTwitchComponent implements OnInit {
  private metasService = inject(MetasTwitchService);

  metas: (MetaTwitch & { seleccionado: boolean })[] = [];
  metaSeleccionada: MetaTwitch | null = null;
  metaEnEdicion: Partial<MetaTwitch> = {};
  filtroEstadoId: string = '';
  terminoBusqueda: string = '';
  paginaActual: number = 1;
  tamanoPagina: number = 5;
  estados: { id: number; nombre_estado: string }[] = [];
  cantidadExportar: number = 0;
  mostrarAlerta: boolean = false;
  tipoAlerta: 'success' | 'danger' = 'success';
  mensajeAlerta: string = '';
  formatoExportacion: 'excel' | 'sql' = 'excel';

  ngOnInit(): void {
    this.cargarMetas();
    this.cargarEstados();
  }

  prepararNuevaMeta(): void {
    this.metaEnEdicion = {};
    this.metaSeleccionada = null;
  }

  prepararEdicionMeta(): void {
    if (this.metaSeleccionada) {
      this.metaEnEdicion = { ...this.metaSeleccionada };
    }
  }

  cargarMetas(callback?: () => void): void {
    this.metasService.getMetas().subscribe({
      next: (data) => {
        this.metas = data.map(m => ({ ...m, seleccionado: false }));
        if (callback) callback();
      },
      error: (err) => {
        this.mostrarMensaje('Error al obtener metas de Twitch', 'danger');
        console.error(err);
      }
    });
  }

  cargarEstados(): void {
    this.metasService.getEstados().subscribe({
      next: (data) => this.estados = data,
      error: (err) => {
        this.mostrarMensaje('Error al obtener estados', 'danger');
        console.error(err);
      }
    });
  }

  mostrarMensaje(mensaje: string, tipo: 'success' | 'danger'): void {
    this.mensajeAlerta = mensaje;
    this.tipoAlerta = tipo;
    this.mostrarAlerta = true;
  }

  contarSeleccionadas(): number {
    return this.metas.filter(m => m.seleccionado).length;
  }

  toggleSeleccionarTodas(event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;
    this.metasFiltradas.forEach(m => m.seleccionado = checked);
  }

  obtenerMetasSeleccionadas(): MetaTwitch[] {
    return this.metas.filter(m => m.seleccionado);
  }

  seleccionarMeta(meta: MetaTwitch): void {
    this.metaSeleccionada = meta;
    this.metaEnEdicion = { ...meta };
  }

  guardarMeta(): void {
    const payload = this.metaEnEdicion;

    if (payload.id_meta) {
      this.metasService.actualizarMeta(payload.id_meta, payload).subscribe({
        next: () => {
          this.mostrarMensaje('Meta actualizada correctamente', 'success');
          this.cargarMetas(() => {
            this.metaSeleccionada = this.metas.find(m => m.id_meta === payload.id_meta) || null;
          });
          this.cerrarModal('modalAgregarMeta');
        },
        error: (err) => {
          this.mostrarMensaje('Error al actualizar meta', 'danger');
          console.error(err);
        }
      });
    } else {
      this.metasService.agregarMeta(payload).subscribe({
        next: (nuevaMeta) => {
          this.mostrarMensaje('Meta agregada correctamente', 'success');
          this.cargarMetas(() => {
            this.metaSeleccionada = this.metas.find(m => m.id_meta === nuevaMeta.id_meta) || null;
          });
          this.metaEnEdicion = {};
          this.cerrarModal('modalAgregarMeta');
        },
        error: (err) => {
          this.mostrarMensaje('Error al agregar meta', 'danger');
          console.error(err);
        }
      });
    }
  }

  eliminarSeleccionadas(): void {
    const seleccionadas = this.obtenerMetasSeleccionadas();
    if (!seleccionadas.length) {
      this.mostrarMensaje('No hay metas seleccionadas', 'danger');
      return;
    }

    const ids = seleccionadas.map(m => m.id_meta!);
    this.metasService.eliminarMultiples(ids).subscribe({
      next: () => {
        this.mostrarMensaje('Metas eliminadas correctamente', 'success');
        this.cargarMetas();
        this.metaSeleccionada = null;
        this.cerrarModal('modalEliminar'); // <-- CIERRA EL MODAL AQUÍ
      },
      error: (err) => {
        this.mostrarMensaje('Error al eliminar metas', 'danger');
        console.error(err);
      }
    });
  }


  abrirModalExportarMetas(): void {
    const seleccionadas = this.obtenerMetasSeleccionadas();
    this.cantidadExportar = seleccionadas.length;

    if (!this.cantidadExportar) {
      this.mostrarMensaje('Selecciona al menos una meta para exportar', 'danger');
      return;
    }

    const modal = document.getElementById('modalConfirmarExportacionMetas');
    if (modal) {
      const bsModal = new (window as any).bootstrap.Modal(modal);
      bsModal.show();
    }
  }

  async confirmarExportacionMetas(): Promise<void> {
    const metasAExportar = this.obtenerMetasSeleccionadas().sort((a, b) => (a.id_meta ?? 0) - (b.id_meta ?? 0));

    if (this.formatoExportacion === 'excel') {
      await this.exportarMetasAExcel(metasAExportar);
    } else {
      await this.exportarMetasASQL(metasAExportar);
    }

    this.cerrarModal('modalConfirmarExportacionMetas');
    this.mostrarMensaje('Exportación completada.', 'success');
  }

  async exportarMetasAExcel(metas: MetaTwitch[]): Promise<void> {
    const workbook = new ExcelJS.Workbook();
    const sheet = workbook.addWorksheet('Metas');

    sheet.columns = [
      { header: 'ID', key: 'id_meta', width: 8 },
      { header: 'Descripción', key: 'descripcion', width: 40 },
      { header: 'Meta', key: 'meta', width: 15 },
      { header: 'Actual', key: 'actual', width: 15 },
      { header: 'Estado ID', key: 'id_estado_metas', width: 15 }
    ];

    metas.forEach(m => {
      sheet.addRow({
        id_meta: m.id_meta,
        descripcion: m.descripcion,
        meta: m.meta,
        actual: m.actual,
        id_estado_metas: m.id_estado_metas
      });
    });

    const buffer = await workbook.xlsx.writeBuffer();
    FileSaver.saveAs(new Blob([buffer], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    }), 'metas.xlsx');
  }

  async exportarMetasASQL(metas: MetaTwitch[]): Promise<void> {
    const inserts = metas.map(m => {
      const safe = (v: any) => v != null ? `'${String(v).replace(/'/g, "''")}'` : 'NULL';
      return `INSERT INTO metas_twitch (descripcion, meta, actual, fecha_inicio, fecha_fin, id_estado_metas)
        VALUES (
          ${safe(m.descripcion)},
          ${m.meta ?? 'NULL'},
          ${m.actual ?? 'NULL'},
          ${safe(m.fecha_inicio)},
          ${safe(m.fecha_fin)},
          ${m.id_estado_metas ?? 'NULL'}
        );`;
    });

    const blob = new Blob([inserts.join('\n\n')], { type: 'text/plain;charset=utf-8' });
    FileSaver.saveAs(blob, 'metas.sql');
  }

  limpiarFiltros(): void {
    this.filtroEstadoId = '';
    this.terminoBusqueda = '';
    this.paginaActual = 1;
    this.metaSeleccionada = null;
    this.metaEnEdicion = {};
    this.metas.forEach(m => m.seleccionado = false);
  }

  get metasFiltradas(): MetaTwitch[] {
    return this.metas.filter(meta =>
      (!this.filtroEstadoId || meta.id_estado_metas === +this.filtroEstadoId) &&
      meta.descripcion.toLowerCase().includes(this.terminoBusqueda.toLowerCase())
    );
  }

  get totalPaginas(): number {
    return Math.ceil(this.metasFiltradas.length / this.tamanoPagina);
  }

  get metasPaginadas(): MetaTwitch[] {
    const inicio = (this.paginaActual - 1) * this.tamanoPagina;
    return this.metasFiltradas.slice(inicio, inicio + this.tamanoPagina);
  }

  irPrimeraPagina(): void {
    this.paginaActual = 1;
  }

  irUltimaPagina(): void {
    this.paginaActual = this.totalPaginas;
  }

  irPaginaAnterior(): void {
    if (this.paginaActual > 1) this.paginaActual--;
  }

  irPaginaSiguiente(): void {
    if (this.paginaActual < this.totalPaginas) this.paginaActual++;
  }

  cerrarModal(id: string): void {
    const modal = document.getElementById(id);
    const btn = modal?.querySelector('[data-bs-dismiss="modal"]') as HTMLElement;
    btn?.click();
  }

  formatearFecha(fecha: string | Date | null | undefined): string {
    if (!fecha) return '-';
    const d = new Date(fecha);
    return `${d.getDate().toString().padStart(2, '0')}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getFullYear()}`;
  }

  estanTodasSeleccionadas(): boolean {
    return this.metasFiltradas.length > 0 && this.metasFiltradas.every(m => m.seleccionado);
  }
}
