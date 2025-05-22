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
  filtroEstadoId: number | undefined;
  estados: { id_estado: number; nombre: string }[] = [];

  // Paginación
  paginaActual: number = 1;
  tamanoPagina: number = 9;

  // Alerta
  mensajeAlerta: string | null = null;
  tipoAlerta: 'success' | 'danger' = 'success';

  // Formulario
  nuevoModerador: Partial<Moderador> = {
    nombre: '',
    email: '',
    id_estado: undefined
  };

  constructor(private moderadoresService: ModeradoresService) { }

  ngOnInit(): void {
    this.cargarModeradores();
    this.cargarEstados();
  }

  // 🟡 ALERTA (sin temporizador automático)
  mostrarAlerta(mensaje: string, tipo: 'success' | 'danger'): void {
    this.mensajeAlerta = mensaje;
    this.tipoAlerta = tipo;
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

  limpiarSeleccion(): void {
    this.moderadorSeleccionado = null;
    this.limpiarFormulario();
  }

  guardarModerador(): void {
    const { nombre, email, id_estado } = this.nuevoModerador;

    if (!nombre?.trim() || !email?.trim() || id_estado === undefined) {
      this.mostrarAlerta('❗ Todos los campos son obligatorios.', 'danger');
      return;
    }

    this.moderadoresService.agregarModerador(this.nuevoModerador).subscribe({
      next: () => {
        this.mostrarAlerta('✅ Moderador guardado correctamente', 'success');
        this.limpiarFormulario();
        this.cargarModeradores();
      },
      error: () => {
        this.mostrarAlerta('❌ Error al guardar el moderador', 'danger');
      }
    });
  }

  // 🔍 FILTRO
  get moderadoresFiltrados(): Moderador[] {
    return this.moderadores.filter(m => {
      const coincideNombre = m.nombre.toLowerCase().includes(this.terminoBusqueda.toLowerCase());
      const coincideEstado = this.filtroEstadoId !== undefined
        ? m.id_estado === this.filtroEstadoId
        : true;
      return coincideNombre && coincideEstado;
    });
  }

  // 🔢 PAGINACIÓN
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
    if (this.paginaActual > 1) this.paginaActual--;
  }

  irPaginaSiguiente(): void {
    if (this.paginaActual < this.totalPaginas) this.paginaActual++;
  }

  // ✅ MULTI-SELECCIÓN
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

  // ⬆️ DAR DE ALTA
  darDeAlta(): void {
    if (!this.moderadorSeleccionado) return;

    const id = this.moderadorSeleccionado.id_moderador;

    this.moderadoresService.darDeAlta(id).subscribe({
      next: () => {
        this.mostrarAlerta('✅ Moderador dado de alta correctamente', 'success');
        this.cargarModeradores();
      },
      error: () => {
        this.mostrarAlerta('❌ Error al dar de alta al moderador', 'danger');
      }
    });
  }


  // 🗓️ FORMATO DE FECHA
  formatearFecha(fecha: string | null): string {
    if (!fecha) return '-';
    const [year, month, day] = fecha.split('-');
    return `${day}-${month}-${year}`;
  }
}
