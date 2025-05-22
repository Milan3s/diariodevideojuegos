import { Component, OnInit } from '@angular/core';
import { NgIf, NgFor, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { JuegosService, Juego } from './juegos.service';
// Agrega esto al comienzo de tu JuegosComponent
import * as ExcelJS from 'exceljs';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-juegos',
  standalone: true,
  imports: [NgIf, NgFor, NgClass, FormsModule, HttpClientModule],
  templateUrl: './juegos.component.html',
  styleUrls: ['./juegos.component.css']
})
export class JuegosComponent implements OnInit {
  juegos: Juego[] = [];
  juegoSeleccionado: Juego | null = null;
  nuevoJuego: Partial<Juego> = this.getNuevoJuegoInicial();
  archivoImagen!: File;
  archivoVideo!: File;

  imagenError = false;
  videoError = false;

  estados: any[] = [];
  consolas: any[] = [];
  juegosSeleccionados: Juego[] = [];


  filtroEstado: number | null = null;
  filtroConsolas: number | null = null;
  terminoBusqueda = '';

  paginaActual = 1;
  elementosPorPagina = 10;

  mensajeAlerta = '';
  tipoAlerta: 'success' | 'danger' = 'success';
  mostrarAlerta = false;
  formatoExportacion: 'excel' | 'sql' = 'excel';
  cantidadExportar: number = 0;

  constructor(private juegosService: JuegosService) { }

  ngOnInit(): void {
    this.cargarJuegos();
    this.cargarEstados();
    this.cargarConsolas();
  }

  mostrarMensaje(texto: string, tipo: 'success' | 'danger') {
    this.mensajeAlerta = texto;
    this.tipoAlerta = tipo;
    this.mostrarAlerta = true;
    setTimeout(() => (this.mostrarAlerta = false), 4000);
  }

  cargarJuegos(): void {
    this.juegosService.obtenerJuegos().subscribe({
      next: (data) => (this.juegos = data),
      error: (err) => console.error('Error cargando juegos:', err)
    });
  }

  cargarEstados(): void {
    this.juegosService.obtenerEstados().subscribe({
      next: (data) => (this.estados = data),
      error: (err) => console.error('Error cargando estados:', err)
    });
  }

  cargarConsolas(): void {
    this.juegosService.obtenerConsolas().subscribe({
      next: (data) => (this.consolas = data),
      error: (err) => console.error('Error cargando consolas:', err)
    });
  }

  abrirModalExportarJuegos(): void {
    const seleccionados = this.obtenerJuegosSeleccionados();
    this.cantidadExportar = seleccionados.length;

    const modal = document.getElementById('modalConfirmarExportacionJuegos');
    if (modal) {
      const bsModal = new (window as any).bootstrap.Modal(modal);
      bsModal.show();
    }
  }

  async confirmarExportacionJuegos(): Promise<void> {
    const seleccionados = this.obtenerJuegosSeleccionados();
    const juegosAExportar = [...(seleccionados.length > 0 ? seleccionados : this.juegos)]
      .sort((a, b) => (a.id_juegos ?? 0) - (b.id_juegos ?? 0));

    if (this.formatoExportacion === 'excel') {
      await this.exportarJuegosAExcel(juegosAExportar);
    }

    if (this.formatoExportacion === 'sql') {
      const insertStatements = juegosAExportar.map(j => {
        const values = [
          this.sqlValue(j.nombre),
          this.sqlValue(j.descripcion),
          this.sqlValue(j.desarrollador),
          this.sqlValue(j.editor),
          this.sqlValue(j.genero),
          this.sqlValue(j.modo_juego),
          this.sqlValue(j.fecha_lanzamiento),
          'NOW()',
          this.sqlValue(j.id_estado),
          this.sqlValue(j.es_recomendado, 'boolean'),
          this.sqlValue(j.imagen),
          this.sqlValue(j.video),
          this.sqlValue(j.region),
          this.sqlValue(j.caja_original, 'boolean'),
          this.sqlValue(j.precintado, 'boolean')
        ];

        return `INSERT INTO juegos (
        nombre, descripcion, desarrollador, editor, genero, modo_juego,
        fecha_lanzamiento, fecha_creacion_registro, id_estado, es_recomendado,
        imagen, video, region, caja_original, precintado
      ) VALUES (${values.join(', ')});`;
      });

      const contenido = insertStatements.join('\n\n');
      const blob = new Blob([contenido], { type: 'text/plain;charset=utf-8' });
      FileSaver.saveAs(blob, 'juegos.sql');
    }

    this.cerrarModal('modalConfirmarExportacionJuegos');
  }


  async exportarJuegosAExcel(juegosAExportar: Juego[]): Promise<void> {
    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet('Juegos');

    worksheet.columns = [
      { header: 'ID', key: 'id_juegos', width: 8 },
      { header: 'Nombre', key: 'nombre', width: 40 },
      { header: 'Descripción', key: 'descripcion', width: 50 },
      { header: 'Desarrollador', key: 'desarrollador', width: 25 },
      { header: 'Editor', key: 'editor', width: 25 },
      { header: 'Género', key: 'genero', width: 20 },
      { header: 'Modo de juego', key: 'modo_juego', width: 20 },
      { header: 'Fecha lanzamiento', key: 'fecha_lanzamiento', width: 20 },
      { header: 'ID Estado', key: 'id_estado', width: 12 },
      { header: 'Recomendado', key: 'es_recomendado', width: 15 },
      { header: 'Imagen', key: 'imagen', width: 30 },
      { header: 'Video', key: 'video', width: 30 },
      { header: 'Región', key: 'region', width: 15 },
      { header: 'Caja original', key: 'caja_original', width: 15 },
      { header: 'Precintado', key: 'precintado', width: 15 }
    ];

    juegosAExportar.forEach(j => {
      worksheet.addRow({
        id_juegos: j.id_juegos,
        nombre: j.nombre || '',
        descripcion: j.descripcion || '',
        desarrollador: j.desarrollador || '',
        editor: j.editor || '',
        genero: j.genero || '',
        modo_juego: j.modo_juego || '',
        fecha_lanzamiento: j.fecha_lanzamiento || '',
        id_estado: j.id_estado ?? '',
        es_recomendado: j.es_recomendado === 1 ? 'Sí' : 'No',
        imagen: j.imagen || '',
        video: j.video || '',
        region: j.region || '',
        caja_original: j.caja_original ? 'Sí' : 'No',
        precintado: j.precintado ? 'Sí' : 'No'
      });
    });

    const headerRow = worksheet.getRow(1);
    headerRow.height = 30;
    headerRow.eachCell(cell => {
      cell.font = { name: 'Calibri', size: 13, bold: true, color: { argb: 'FFFFFFFF' } };
      cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '4472C4' } };
      cell.alignment = { horizontal: 'center', vertical: 'middle', wrapText: true };
      cell.border = {
        top: { style: 'thin' }, left: { style: 'thin' },
        bottom: { style: 'thin' }, right: { style: 'thin' }
      };
    });

    worksheet.eachRow((row, rowNumber) => {
      if (rowNumber !== 1) row.height = 22;
      row.eachCell(cell => {
        cell.font = { name: 'Calibri', size: 13 };
        cell.alignment = { vertical: 'middle', horizontal: 'center' };
        cell.border = {
          top: { style: 'thin' }, left: { style: 'thin' },
          bottom: { style: 'thin' }, right: { style: 'thin' }
        };
      });
    });

    const buffer = await workbook.xlsx.writeBuffer();
    const blob = new Blob([buffer], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    });
    FileSaver.saveAs(blob, 'juegos.xlsx');
  }


  sqlValue(value: any, type: 'string' | 'boolean' = 'string'): string {
    if (value === undefined || value === null || value === '') return 'NULL';
    if (type === 'boolean') return value ? '1' : '0';
    const str = value.toString().replace(/'/g, "''");
    return `'${str}'`;
  }

  // Seleccionar/deseleccionar todos los juegos visibles en la página actual
  toggleSeleccionarTodos(checked: boolean): void {
    this.juegosFiltrados().forEach(j => j.seleccionado = checked);
  }

  cantidadSeleccionados(): number {
    return this.obtenerJuegosSeleccionados().length;
  }

  onSeleccionarTodosChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input) {
      this.toggleSeleccionarTodos(input.checked);
    }
  }


  // Obtener todos los juegos seleccionados (de todas las páginas)
  obtenerJuegosSeleccionados(): Juego[] {
    return this.juegos.filter(j => j.seleccionado);
  }

  estanTodosSeleccionados(): boolean {
    const juegosFiltrados = this.juegosFiltrados();
    return juegosFiltrados.length > 0 && juegosFiltrados.every(j => j.seleccionado);
  }


  eliminarSeleccionados(): void {
    const juegosSeleccionados = this.obtenerJuegosSeleccionados();
    if (juegosSeleccionados.length === 0) {
      this.mostrarMensaje('No hay juegos seleccionados.', 'danger');
      return;
    }

    const ids = juegosSeleccionados.map(j => j.id_juegos).filter(id => id != null) as number[];

    if (ids.length === 1) {
      // Eliminar solo uno
      this.juegosService.eliminarJuego(ids[0]).subscribe({
        next: () => {
          this.mostrarMensaje('Juego eliminado.', 'success');
          this.cargarJuegos();
        },
        error: () => this.mostrarMensaje('Error al eliminar.', 'danger')
      });
    } else {
      // Eliminar múltiples
      this.juegosService.eliminarVariosJuegos(ids).subscribe({
        next: () => {
          this.mostrarMensaje('Juegos eliminados correctamente.', 'success');
          this.cargarJuegos();
        },
        error: () => this.mostrarMensaje('Error al eliminar varios juegos.', 'danger')
      });
    }
  }



  seleccionarJuego(juego: Juego): void {
    this.juegoSeleccionado = juego;
    this.imagenError = false;
    this.videoError = false;

    this.nuevoJuego = {
      id_consola: juego.id_consola ?? null,
      id_estado: juego.id_estado,
      nombre: juego.nombre,
      genero: juego.genero,
      editor: juego.editor,
      desarrollador: juego.desarrollador,
      modo_juego: juego.modo_juego,
      fecha_lanzamiento: juego.fecha_lanzamiento,
      es_recomendado: juego.es_recomendado ?? 1,
      descripcion: juego.descripcion,
      region: juego.region,
      caja_original: juego.caja_original ?? false,
      precintado: juego.precintado ?? false,
      consola_abreviatura: ''
    };
  }

  limpiarSeleccion(): void {
    this.juegoSeleccionado = null;
    this.nuevoJuego = this.getNuevoJuegoInicial();
    this.archivoImagen = undefined!;
    this.archivoVideo = undefined!;
  }

  onArchivoSeleccionado(event: Event, tipo: 'imagen' | 'video') {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      if (tipo === 'imagen') this.archivoImagen = input.files[0];
      if (tipo === 'video') this.archivoVideo = input.files[0];
    }
  }



  guardarJuego(): void {
    console.log('Datos del nuevo juego:', this.nuevoJuego);

    const formData = this.prepararFormData();

    for (const pair of formData.entries()) {
      console.log('FormData ->', pair[0], ':', pair[1]);
    }

    this.juegosService.agregarJuego(formData).subscribe({
      next: () => {
        this.resetFormulario();
        this.mostrarMensaje('Juego guardado correctamente.', 'success');
        this.cerrarModal('modalAgregarJuego');
      },
      error: (err) => {
        console.error('Error guardando juego:', err);
        this.mostrarMensaje('Error al guardar el juego.', 'danger');
      }
    });
  }


  confirmarEditarJuego(): void {
    if (!this.juegoSeleccionado?.id_juegos) return;
    const formData = this.prepararFormData();

    this.juegosService.actualizarJuego(this.juegoSeleccionado.id_juegos, formData).subscribe({
      next: () => {
        this.resetFormulario();
        this.mostrarMensaje('Juego actualizado correctamente.', 'success');
        this.cerrarModal('modalEditarJuego');
      },
      error: (err) => {
        console.error('Error actualizando juego:', err);
        this.mostrarMensaje('Error al actualizar el juego.', 'danger');
      }
    });
  }

  private prepararFormData(): FormData {
    const formData = new FormData();

    for (const key in this.nuevoJuego) {
      if (key === 'consola_abreviatura') continue;
      const valor = (this.nuevoJuego as any)[key];
      if (valor !== undefined && valor !== null) {
        formData.append(key, valor.toString());
      }
    }

    const abreviaturaManual = this.nuevoJuego.consola_abreviatura?.trim().toLowerCase() || 'default';
    formData.append('consola_abreviatura', abreviaturaManual);

    if (this.archivoImagen) formData.append('imagen', this.archivoImagen);
    if (this.archivoVideo) formData.append('video', this.archivoVideo);

    return formData;
  }

  private resetFormulario(): void {
    this.nuevoJuego = this.getNuevoJuegoInicial();
    this.archivoImagen = undefined!;
    this.archivoVideo = undefined!;
    this.limpiarSeleccion();
    this.cargarJuegos();
  }

  juegosFiltrados(): Juego[] {
    return this.juegos.filter(juego =>
      (!this.filtroEstado || juego.id_estado === this.filtroEstado) &&
      (!this.filtroConsolas || juego.consola_nombre?.includes(this.nombreConsolaPorId(this.filtroConsolas))) &&
      (!this.terminoBusqueda || juego.nombre.toLowerCase().includes(this.terminoBusqueda.toLowerCase()))
    );
  }

  nombreConsolaPorId(id: number): string {
    const consola = this.consolas.find(c => c.id_consola === id);
    return consola?.nombre || '';
  }

  juegosPaginados(): Juego[] {
    const juegos = this.juegosFiltrados();
    const inicio = (this.paginaActual - 1) * this.elementosPorPagina;
    return juegos.slice(inicio, inicio + this.elementosPorPagina);
  }

  totalPaginas(): number {
    return Math.ceil(this.juegosFiltrados().length / this.elementosPorPagina);
  }

  irPrimeraPagina(): void { this.paginaActual = 1; }
  irUltimaPagina(): void { this.paginaActual = this.totalPaginas(); }
  irPaginaAnterior(): void { if (this.paginaActual > 1) this.paginaActual--; }
  irPaginaSiguiente(): void { if (this.paginaActual < this.totalPaginas()) this.paginaActual++; }

  limpiarFiltros(): void {
    this.filtroEstado = null;
    this.filtroConsolas = null;
    this.terminoBusqueda = '';
    this.paginaActual = 1;
    this.nuevoJuego = this.getNuevoJuegoInicial();
    this.limpiarSeleccion();
  }

  confirmarEliminarJuego(): void {
    if (!this.juegoSeleccionado?.id_juegos) return;

    this.juegosService.eliminarJuego(this.juegoSeleccionado.id_juegos).subscribe({
      next: () => {
        this.mostrarMensaje('Juego eliminado correctamente.', 'success');
        this.cargarJuegos();
        this.limpiarSeleccion();
        this.cerrarModal('modalConfirmarEliminacion');
      },
      error: (err: unknown) => {
        console.error('Error eliminando juego:', err);
        this.mostrarMensaje('Error al eliminar el juego.', 'danger');
        this.cerrarModal('modalConfirmarEliminacion');
      }
    });
  }

  confirmarEliminarSeleccionados(): void {
    const juegosSeleccionados = this.obtenerJuegosSeleccionados();
    const ids = juegosSeleccionados.map(j => j.id_juegos).filter(id => id != null) as number[];

    if (ids.length === 0) {
      this.mostrarMensaje('No hay juegos seleccionados.', 'danger');
      return;
    }

    this.juegosService.eliminarVariosJuegos(ids).subscribe({
      next: () => {
        this.mostrarMensaje('Juegos eliminados correctamente.', 'success');
        this.cargarJuegos();
        this.cerrarModal('modalConfirmarEliminacionMultiple');
      },
      error: () => {
        this.mostrarMensaje('Error al eliminar varios juegos.', 'danger');
        this.cerrarModal('modalConfirmarEliminacionMultiple');
      }
    });
  }

  formatearFecha(fecha: string | Date | null | undefined): string {
    if (!fecha) return '-';
    const d = new Date(fecha);
    const dia = d.getDate().toString().padStart(2, '0');
    const mes = (d.getMonth() + 1).toString().padStart(2, '0');
    const anio = d.getFullYear();
    return `${dia}-${mes}-${anio}`;
  }



  private cerrarModal(modalId: string): void {
    const modalEl = document.getElementById(modalId);
    if (modalEl) {
      const btnClose = modalEl.querySelector('[data-bs-dismiss="modal"]') as HTMLElement;
      btnClose?.click();
    }
  }
  private getNuevoJuegoInicial(): Partial<Juego> {
    return {
      nombre: '',
      genero: '',
      editor: '',
      desarrollador: '',
      modo_juego: '',
      descripcion: '',
      region: '',
      consola_abreviatura: '',
      es_recomendado: undefined,
      caja_original: false,
      precintado: false,
      id_estado: undefined,          // ✅ en lugar de null para evitar error de tipo
      id_consola: undefined,         // ✅ también
      fecha_lanzamiento: undefined   // ✅ evita conflicto de tipo
    };
  }


}