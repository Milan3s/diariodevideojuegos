import { Component, OnInit } from '@angular/core';
import { NgIf, NgFor, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ConsolasService, Consola } from './consolas.service';
import * as FileSaver from 'file-saver';
import * as ExcelJS from 'exceljs';

@Component({
  selector: 'app-consolas',
  standalone: true,
  imports: [NgIf, NgFor, NgClass, FormsModule, HttpClientModule],
  providers: [ConsolasService],
  templateUrl: './consolas.component.html',
  styleUrls: ['./consolas.component.css']
})
export class ConsolasComponent implements OnInit {
  consolas: (Consola & { seleccionado?: boolean })[] = [];
  consolaSeleccionada: Consola | null = null;
  nuevaConsola: Partial<Consola> = {};
  archivoImagen!: File;
  archivoVideo!: File;

  imagenError = false;
  videoError = false;
  estados: any[] = [];

  filtroEstado: number | null = null;
  terminoBusqueda = '';
  paginaActual = 1;
  elementosPorPagina = 10;
  mensajeAlerta = '';
  tipoAlerta: 'success' | 'danger' = 'success';
  mostrarAlerta = false;
  cantidadExportar = 0;
  formatoExportacion: 'excel' | 'sql' = 'excel';

  constructor(private consolasService: ConsolasService) { }

  ngOnInit(): void {
    this.cargarConsolas();
    this.cargarEstados();

  }

  formatearFecha(fecha: string | Date | null | undefined): string {
    if (!fecha) return '-';
    const d = new Date(fecha);
    const dia = d.getDate().toString().padStart(2, '0');
    const mes = (d.getMonth() + 1).toString().padStart(2, '0');
    const anio = d.getFullYear();
    return `${dia}-${mes}-${anio}`;
  }

  generarCarpetaDesdeAbreviatura(abreviatura?: string): string {
    return abreviatura
      ? abreviatura.trim().toLowerCase().replace(/\s+/g, '_')
      : 'default';
  }


  onArchivoSeleccionado(event: Event, tipo: 'imagen' | 'video'): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      const archivo = input.files[0];
      if (tipo === 'imagen') {
        this.archivoImagen = archivo;
      } else {
        this.archivoVideo = archivo;
      }
    }
  }

  abrirModalExportar(): void {
    const seleccionadas = this.consolas.filter(c => c.seleccionado);
    this.cantidadExportar = seleccionadas.length;
    const modal = document.getElementById('modalConfirmarExportacion');
    if (modal) new (window as any).bootstrap.Modal(modal).show();
  }

  async confirmarExportacion(): Promise<void> {
    const seleccionadas = this.consolas.filter(c => c.seleccionado);
    const consolasAExportar = [...(seleccionadas.length > 0 ? seleccionadas : this.consolas)]
      .sort((a, b) => (a.id_consola ?? 0) - (b.id_consola ?? 0));

    if (this.formatoExportacion === 'excel') {
      await this.exportarConsolasAExcel(consolasAExportar);
    } else {
      const insertStatements = consolasAExportar.map(c => {
        const values = [
          this.sqlValue(c.nombre),
          this.sqlValue(c.abreviatura),
          this.sqlValue(c.anio),
          this.sqlValue(c.fabricante),
          this.sqlValue(c.generacion),
          this.sqlValue(c.region),
          this.sqlValue(c.tipo),
          this.sqlValue(c.procesador),
          this.sqlValue(c.memoria),
          this.sqlValue(c.almacenamiento),
          this.sqlValue(c.frecuencia_mhz),
          this.sqlValue(c.chip, 'boolean'),
          this.sqlValue(c.caracteristicas),
          this.sqlValue(c.fecha_lanzamiento),
          this.sqlValue(c.imagen),
          this.sqlValue(c.video),
          this.sqlValue(c.id_estado),
          this.sqlValue(c.original, 'boolean'),
          this.sqlValue(c.modificada, 'boolean'),
          this.sqlValue(c.caja, 'boolean'),
          this.sqlValue(c.precintada, 'boolean'),
          this.sqlValue(c.hz)
        ];

        return `INSERT INTO consolas (nombre, abreviatura, anio, fabricante, generacion, region, tipo, procesador, memoria, almacenamiento, frecuencia_mhz, chip, caracteristicas, fecha_lanzamiento, imagen, video, id_estado, original, modificada, caja, precintada, hz) VALUES (${values.join(', ')});`;
      });

      const blob = new Blob([insertStatements.join('\n\n')], { type: 'text/plain;charset=utf-8' });
      FileSaver.saveAs(blob, 'consolas.sql');
    }

    this.cerrarModal('modalConfirmarExportacion');
  }

  async exportarConsolasAExcel(consolasAExportar: Consola[]): Promise<void> {
    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet('Consolas');

    const columnas = [
      'ID', 'Nombre', 'Abreviatura', 'Año', 'Fabricante', 'Generación', 'Región', 'Tipo', 'Procesador',
      'Memoria', 'Almacenamiento', 'Frecuencia MHz', 'Chip dedicado', 'Características',
      'Fecha lanzamiento', 'Imagen', 'Video', 'Estado ID', 'Original', 'Modificada',
      'Caja', 'Precintada', 'Hz', 'Estado nombre', 'Juegos asociados'
    ];

    worksheet.columns = columnas.map(t => ({ header: t, key: t.toLowerCase().replace(/\s+/g, '_'), width: 40 }));

    consolasAExportar.forEach(c => {
      worksheet.addRow({
        id: c.id_consola,
        nombre: c.nombre || '',
        abreviatura: c.abreviatura || '',
        fabricante: c.fabricante || '',
        generación: c.generacion || '',
        región: c.region || '',
        tipo: c.tipo || '',
        procesador: c.procesador || '',
        memoria: c.memoria || '',
        almacenamiento: c.almacenamiento || '',
        frecuencia_mhz: c.frecuencia_mhz || '',
        chip_dedicado: c.chip ? 'Sí' : 'No',
        características: c.caracteristicas || '',
        fecha_lanzamiento: c.fecha_lanzamiento || '',
        imagen: c.imagen || '',
        video: c.video || '',
        estado_id: c.id_estado ?? '',
        original: c.original ? 'Sí' : 'No',
        modificada: c.modificada ? 'Sí' : 'No',
        caja: c.caja ? 'Sí' : 'No',
        precintada: c.precintada ? 'Sí' : 'No',
        hz: c.hz || '',
        estado_nombre: c.estado_nombre || '',
        juegos_asociados: c.cantidad_juegos ?? 0
      });
    });

    const headerRow = worksheet.getRow(1);
    headerRow.height = 30;
    headerRow.eachCell(cell => {
      cell.font = { name: 'Calibri', size: 13, bold: true, color: { argb: 'FFFFFFFF' } };
      cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '4472C4' } };
      cell.alignment = { horizontal: 'center', vertical: 'middle', wrapText: true };
      cell.border = { top: { style: 'thin' }, left: { style: 'thin' }, bottom: { style: 'thin' }, right: { style: 'thin' } };
    });

    worksheet.eachRow((row, rowNumber) => {
      if (rowNumber !== 1) row.height = 22;
      row.eachCell(cell => {
        cell.font = { name: 'Calibri', size: 13 };
        cell.alignment = { vertical: 'middle', horizontal: 'center' };
        cell.border = { top: { style: 'thin' }, left: { style: 'thin' }, bottom: { style: 'thin' }, right: { style: 'thin' } };
      });
    });

    const buffer = await workbook.xlsx.writeBuffer();
    FileSaver.saveAs(new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }), 'consolas.xlsx');
  }

  cantidadSeleccionadas(): number {
    return this.consolas.filter(c => c.seleccionado).length;
  }

  confirmarEliminarSeleccionadas(): void {
    const ids = this.consolas.filter(c => c.seleccionado && c.id_consola).map(c => c.id_consola);
    if (ids.length === 0) return;
    this.consolasService.eliminarVariasConsolas(ids).subscribe({
      next: () => {
        this.mostrarMensaje('Consolas eliminadas correctamente.', 'success');
        this.cargarConsolas();
        this.limpiarSeleccion();
        this.cerrarModal('modalConfirmarEliminacionMultiple');
      },
      error: () => this.mostrarMensaje('Error al eliminar consolas.', 'danger')
    });
  }

  toggleSeleccionarTodas(event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;
    this.consolasFiltradas().forEach(c => c.seleccionado = checked);
  }

  estanTodasSeleccionadas(): boolean {
    const filtradas = this.consolasFiltradas();
    return filtradas.length > 0 && filtradas.every(c => c.seleccionado);
  }

  seleccionarConsola(consola: Consola): void {
    this.consolaSeleccionada = consola;
    this.imagenError = false;
    this.videoError = false;

    this.nuevaConsola = {
      ...consola,
      chip: this.convertirChipABooleano(consola.chip),
      original: consola.original ?? true,
      modificada: consola.modificada ?? false,
      caja: consola.caja ?? false,
      precintada: consola.precintada ?? false
    };
  }

  mostrarBooleano(valor: boolean | null | undefined): string {
    return valor === true ? 'Sí' : valor === false ? 'No' : '-';
  }



  private convertirChipABooleano(valor: any): boolean | undefined {
    if (valor === 1 || valor === '1' || valor === true) return true;
    if (valor === 0 || valor === '0' || valor === false) return false;
    return undefined;
  }


  actualizarBooleanosDesdeRadio(campo: 'original' | 'modificada' | 'caja' | 'precintada'): void {
    if (campo === 'original') { this.nuevaConsola.original = true; this.nuevaConsola.modificada = false; }
    if (campo === 'modificada') { this.nuevaConsola.original = false; this.nuevaConsola.modificada = true; }
    if (campo === 'caja') { this.nuevaConsola.caja = true; this.nuevaConsola.precintada = false; }
    if (campo === 'precintada') { this.nuevaConsola.caja = false; this.nuevaConsola.precintada = true; }
  }

  limpiarSeleccion(): void {
    this.consolaSeleccionada = null;
    this.nuevaConsola = {};
    this.archivoImagen = undefined!;
    this.archivoVideo = undefined!;
    this.consolas.forEach(c => c.seleccionado = false);
  }

  mostrarSiNo(valor: number | boolean | null | undefined): string {
    return valor === 1 || valor === true
      ? 'Sí'
      : valor === 0 || valor === false
        ? 'No'
        : '-';
  }


  cargarConsolas(): void {
    this.consolasService.obtenerConsolas().subscribe({
      next: data => {
        this.consolas = data.map(c => ({ ...c, seleccionado: false }));
      },
      error: err => console.error('Error cargando consolas:', err)
    });
  }



  cargarEstados(): void {
    this.consolasService.getEstadosConsola().subscribe({
      next: data => this.estados = data,
      error: err => console.error('Error cargando estados:', err)
    });
  }

  cerrarModal(modalId: string): void {
    const modalEl = document.getElementById(modalId);
    if (modalEl) {
      const btnClose = modalEl.querySelector('[data-bs-dismiss="modal"]') as HTMLElement;
      btnClose?.click();
    }
  }

  mostrarMensaje(texto: string, tipo: 'success' | 'danger'): void {
    this.mensajeAlerta = texto;
    this.tipoAlerta = tipo;
    this.mostrarAlerta = true;
    // ❌ Quitar esto porque hace que se cierre sola de inmediato:
    // setTimeout(() => (this.mostrarAlerta = false));
  }


  consolasFiltradas(): Consola[] {
    return this.consolas.filter(c =>
      (!this.filtroEstado || c.id_estado === this.filtroEstado) &&
      (!this.terminoBusqueda || c.nombre?.toLowerCase().includes(this.terminoBusqueda.toLowerCase()))
    );
  }

  consolasPaginadas(): Consola[] {
    const inicio = (this.paginaActual - 1) * this.elementosPorPagina;
    return this.consolasFiltradas().slice(inicio, inicio + this.elementosPorPagina);
  }

  totalPaginas(): number {
    return Math.ceil(this.consolasFiltradas().length / this.elementosPorPagina);
  }

  irPrimeraPagina(): void { this.paginaActual = 1; }
  irUltimaPagina(): void { this.paginaActual = this.totalPaginas(); }
  irPaginaAnterior(): void { if (this.paginaActual > 1) this.paginaActual--; }
  irPaginaSiguiente(): void { if (this.paginaActual < this.totalPaginas()) this.paginaActual++; }

  limpiarFiltros(): void {
    this.filtroEstado = null;
    this.terminoBusqueda = '';
    this.paginaActual = 1;
    this.limpiarSeleccion();
  }

  prepararFormData(): FormData {
    const formData = new FormData();

    // 🔁 Agregar campos de nuevaConsola
    for (const key of Object.keys(this.nuevaConsola)) {
      const valor = (this.nuevaConsola as any)[key];
      if (valor !== undefined && valor !== null) {
        if (typeof valor === 'boolean') {
          formData.append(key, valor ? '1' : '0');
        } else if (valor instanceof Date) {
          formData.append(key, valor.toISOString().split('T')[0]); // yyyy-mm-dd
        } else {
          formData.append(key, valor.toString());
        }
      }
    }

    // ✅ Asegurar compatibilidad con Multer (campo manual obligatorio)
    const carpeta = this.generarCarpetaDesdeAbreviatura(this.nuevaConsola.abreviatura);
    formData.append('consola_abreviatura', carpeta);

    // 📎 Adjuntar archivos si están definidos
    if (this.archivoImagen) {
      formData.append('imagen', this.archivoImagen);
    }

    if (this.archivoVideo) {
      formData.append('video', this.archivoVideo);
    }

    for (const pair of formData.entries()) {
      console.log(pair[0], pair[1]);
    }


    return formData;
  }

  esBooleano(value: any): string {
    return value == 1 ? 'Sí' : value == 0 ? 'No' : '-';
  }


  guardarConsola(): void {
    this.consolasService.agregarConsola(this.prepararFormData()).subscribe({
      next: () => {
        this.resetFormulario();
        this.mostrarMensaje('Consola guardada correctamente.', 'success');
        this.cerrarModal('modalAgregarConsola');
      },
      error: () => this.mostrarMensaje('Error al guardar la consola.', 'danger')
    });
  }
  resetFormulario(): void {
    this.limpiarSeleccion();
    this.cargarConsolas();
  }


  confirmarEditarConsola(): void {
    if (!this.consolaSeleccionada?.id_consola) return;

    const idActualizado = this.consolaSeleccionada.id_consola;

    this.consolasService.actualizarConsola(idActualizado, this.prepararFormData()).subscribe({
      next: () => {
        this.cerrarModal('modalAgregarConsola');

        this.mostrarMensaje('Consola actualizada correctamente.', 'success');

        // ✅ Volver a cargar consolas, pero manteniendo la selección
        this.cargarConsolasYReseleccionar(idActualizado);
      },
      error: () => this.mostrarMensaje('Error al actualizar la consola.', 'danger')
    });
  }

  private cargarConsolasYReseleccionar(id: number): void {
    this.consolasService.obtenerConsolas().subscribe({
      next: data => {
        this.consolas = data.map(c => ({ ...c, seleccionado: false }));

        // Reseleccionar la consola recién actualizada
        const actualizada = this.consolas.find(c => c.id_consola === id);
        if (actualizada) {
          this.seleccionarConsola(actualizada);
        }
      },
      error: err => console.error('Error recargando consolas:', err)
    });
  }



  sqlValue(value: any, type: 'string' | 'boolean' = 'string'): string {
    if (value === undefined || value === null || value === '') return 'NULL';
    if (type === 'boolean') return value ? '1' : '0';
    return `'${value.toString().replace(/'/g, "''")}'`;
  }
}