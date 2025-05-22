import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ModeradoresService, Moderador } from './moderadores.service';
import { FormsModule } from '@angular/forms';

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

  // Filtros
  terminoBusqueda: string = '';
  filtroEstadoId: string | null = null;

  estados: { id_estado: number; nombre: string }[] = [];

  // Paginación
  paginaActual: number = 1;
  tamanoPagina: number = 9;

  constructor(private moderadoresService: ModeradoresService) { }

  ngOnInit(): void {
    this.cargarModeradores();
    this.cargarEstados();
  }

  cargarModeradores(): void {
    this.moderadoresService.obtenerModeradores().subscribe({
      next: (data) => {
        this.moderadores = data.map(m => ({
          ...m,
          seleccionado: false
        }));
      },
      error: (err) => {
        console.error('❌ Error al obtener moderadores:', err);
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

  limpiarSeleccion(): void {
    this.moderadorSeleccionado = null;
  }

  // 🔍 Filtrado
  get moderadoresFiltrados(): Moderador[] {
    return this.moderadores.filter(m => {
      const coincideNombre = m.nombre.toLowerCase().includes(this.terminoBusqueda.toLowerCase());
      const coincideEstado =
        this.filtroEstadoId !== null ? m.id_estado?.toString() === this.filtroEstadoId : true;
      return coincideNombre && coincideEstado;
    });
  }


  // ✅ Paginación sobre los filtrados
  get totalPaginas(): number {
    return Math.ceil(this.moderadoresFiltrados.length / this.tamanoPagina) || 1;
  }

  get moderadoresPaginados(): Moderador[] {
    const inicio = (this.paginaActual - 1) * this.tamanoPagina;
    return this.moderadoresFiltrados.slice(inicio, inicio + this.tamanoPagina);
  }

  irPrimeraPagina(): void {
    this.paginaActual = 1;
  }

  irUltimaPagina(): void {
    this.paginaActual = this.totalPaginas;
  }

  irPaginaAnterior(): void {
    if (this.paginaActual > 1) {
      this.paginaActual--;
    }
  }

  irPaginaSiguiente(): void {
    if (this.paginaActual < this.totalPaginas) {
      this.paginaActual++;
    }
  }

  // ✅ Selección múltiple
  estanTodosSeleccionados(): boolean {
    return this.moderadoresFiltrados.length > 0 &&
      this.moderadoresFiltrados.every(m => m.seleccionado);
  }

  cantidadSeleccionados(): number {
    return this.moderadores.filter(m => m.seleccionado).length;
  }

  onSeleccionarTodosChange(event: any): void {
    const checked = event.target.checked;
    this.moderadoresFiltrados.forEach(m => m.seleccionado = checked);
  }
}
