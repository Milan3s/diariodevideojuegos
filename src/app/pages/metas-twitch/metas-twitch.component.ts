import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
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

  filtroEstadoId: string = '';
  terminoBusqueda: string = '';

  paginaActual: number = 1;
  tamanoPagina: number = 5;

  estados: { id: number; nombre_estado: string }[] = [];

  ngOnInit(): void {
    this.cargarMetas();
    this.cargarEstados();
  }

  cargarMetas(): void {
    this.metasService.getMetas().subscribe({
      next: (data) => {
        this.metas = data.map(m => ({ ...m, seleccionado: false }));
      },
      error: (err) => {
        console.error('❌ Error al obtener metas de Twitch:', err);
      }
    });
  }

  cargarEstados(): void {
    this.metasService.getEstados().subscribe({
      next: (data) => {
        this.estados = data;
      },
      error: (err) => {
        console.error('❌ Error al obtener estados de metas:', err);
      }
    });
  }

  // 🔍 Filtro combinado (estado + búsqueda)
  get metasFiltradas(): MetaTwitch[] {
    return this.metas.filter(meta =>
      (!this.filtroEstadoId || meta.id_estado_metas === +this.filtroEstadoId) &&
      meta.descripcion.toLowerCase().includes(this.terminoBusqueda.toLowerCase())
    );
  }

  // 📄 Paginación
  get totalPaginas(): number {
    return Math.ceil(this.metasFiltradas.length / this.tamanoPagina) || 1;
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

  // ✅ Selección
  seleccionarMeta(meta: MetaTwitch): void {
    this.metaSeleccionada = meta;
  }

  estanTodasSeleccionadas(): boolean {
    return this.metasFiltradas.length > 0 &&
           this.metasFiltradas.every(m => m.seleccionado);
  }

  toggleSeleccionarTodas(event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;
    this.metasFiltradas.forEach(meta => meta.seleccionado = checked);
  }

  contarSeleccionadas(): number {
    return this.metas.filter(meta => meta.seleccionado).length;
  }

  limpiarFiltros(): void {
    this.terminoBusqueda = '';
    this.filtroEstadoId = '';
    this.paginaActual = 1;
    this.metaSeleccionada = null;
    this.metas.forEach(m => m.seleccionado = false);
  }
}
