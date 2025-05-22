import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Interface principal para un juego
export interface Juego {
  id_juegos?: number;
  nombre: string;
  descripcion?: string;
  genero?: string;
  editor?: string;
  desarrollador?: string;
  modo_juego?: string;
  fecha_lanzamiento?: string;
  fecha_creacion_registro?: string;
  imagen?: string;
  video?: string;
  region?: string;
  caja_original?: boolean;
  precintado?: boolean;

  id_consola: number | null;
  consola_nombre?: string;
  consola_abreviatura?: string;

  id_estado?: number;
  estado_nombre?: string;

  es_recomendado?: number;
  RutaImagen?: string;
  RutaVideo?: string;
  seleccionado?: boolean;

}

@Injectable({
  providedIn: 'root'
})
export class JuegosService {
  private apiUrl = 'http://localhost:3000/api';

  constructor(private http: HttpClient) { }

  // Obtener todos los juegos
  obtenerJuegos(): Observable<Juego[]> {
    return this.http.get<Juego[]>(`${this.apiUrl}/juegos`);
  }

  // Obtener todos los estados
  obtenerEstados(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/juegos/estados`);
  }

  // Obtener todas las consolas
  obtenerConsolas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/juegos/consolas`);
  }

  // Agregar un nuevo juego
  agregarJuego(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/juegos`, formData);
  }

  // Eliminar un juego
  eliminarJuego(id_juegos: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/juegos/${id_juegos}`);
  }

  // Eliminar varios juegos a la vez
  eliminarVariosJuegos(ids: number[]): Observable<any> {
    return this.http.post(`${this.apiUrl}/juegos/eliminar-multiples`, { ids });
  }


  // Actualizar un juego existente
  actualizarJuego(id_juegos: number, formData: FormData): Observable<any> {
    return this.http.put(`${this.apiUrl}/juegos/${id_juegos}`, formData);
  }
}
