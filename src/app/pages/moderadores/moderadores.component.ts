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

  constructor(private moderadoresService: ModeradoresService) { }

  ngOnInit(): void {
    this.cargarModeradores();
  }

  cargarModeradores(): void {
    this.moderadoresService.obtenerModeradores().subscribe({
      next: (data) => {
        this.moderadores = data.map(m => ({
          ...m,
          seleccionado: false // importante para selección múltiple
        }));
      },
      error: (err) => {
        console.error('❌ Error al obtener moderadores:', err);
      }
    });
  }


  seleccionarModerador(moderador: Moderador): void {
    this.moderadorSeleccionado = moderador;
  }

  limpiarSeleccion(): void {
    this.moderadorSeleccionado = null;
  }

  // ✅ Selección múltiple
  estanTodosSeleccionados(): boolean {
    return this.moderadores.length > 0 && this.moderadores.every(m => m.seleccionado);
  }

  cantidadSeleccionados(): number {
    return this.moderadores.filter(m => m.seleccionado).length;
  }

  onSeleccionarTodosChange(event: any): void {
    const checked = event.target.checked;
    this.moderadores.forEach(m => m.seleccionado = checked);
  }

  // 🔢 Paginación
  paginaActual: number = 1;
  tamanoPagina: number = 7;

  totalPaginas(): number {
    return Math.ceil(this.moderadores.length / this.tamanoPagina) || 1;
  }

  moderadoresPaginados(): Moderador[] {
    const inicio = (this.paginaActual - 1) * this.tamanoPagina;
    return this.moderadores.slice(inicio, inicio + this.tamanoPagina);
  }

  irPrimeraPagina(): void {
    this.paginaActual = 1;
  }

  irUltimaPagina(): void {
    this.paginaActual = this.totalPaginas();
  }

  irPaginaAnterior(): void {
    if (this.paginaActual > 1) {
      this.paginaActual--;
    }
  }

  irPaginaSiguiente(): void {
    if (this.paginaActual < this.totalPaginas()) {
      this.paginaActual++;
    }
  }
}
