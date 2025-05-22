import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Logro {
  id_logro?: number;
  nombre: string;
  descripcion: string;
  horas_estimadas: number;
  anio: number;
  fecha_inicio?: string;
  fecha_fin?: string;
  intentos: number;
  creditos: number;
  puntuacion: number;
  fecha_registro?: string;
  id_juego?: number;
  id_estado?: number;
  id_dificultad?: number;
  id_consola?: number;

  // Enriquecidos con nombres
  juego_nombre?: string;
  estado_nombre?: string;
  dificultad_nombre?: string;
  consola_nombre?: string;

  // Para selección en el listado
  seleccionado?: boolean;
  estado_logro_nombre?: string; // <-- Añade esto
  progreso?: number;

}

@Injectable({
  providedIn: 'root'
})
export class LogrosService {
  private apiUrl = 'http://localhost:3000/api/logros';

  constructor(private http: HttpClient) { }

  obtenerLogros(): Observable<Logro[]> {
    return this.http.get<Logro[]>(this.apiUrl);
  }

  eliminarLogro(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  eliminarVariosLogros(ids: number[]): Observable<any> {
    return this.http.request('delete', 'http://localhost:3000/api/logros', {
      body: { ids }
    });
  }


  agregarLogro(logro: Partial<Logro>): Observable<any> {
    return this.http.post<any>(this.apiUrl, logro);
  }

  actualizarLogro(id: number, logro: Partial<Logro>): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, logro);
  }


  obtenerEstados(): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:3000/api/estados?tipo=logro`);
  }

  obtenerConsolas(): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:3000/api/consolas`);
  }

  obtenerJuegos(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:3000/api/logros/juegos'); // ✅ Correcta
  }

  obtenerDificultades(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:3000/api/logros/dificultades'); // ✅ Correcta
  }



  limpiarSeleccionados(logros: Logro[]): Logro[] {
    return logros.map(logro => ({ ...logro, seleccionado: false }));
  }

  limpiarFiltros(): Partial<{
    terminoBusqueda: string;
    filtroEstado: number | null;
    filtroConsola: number | null;
    paginaActual: number;
    logroSeleccionado: Logro | null;
  }> {
    return {
      terminoBusqueda: '',
      filtroEstado: null,
      filtroConsola: null,
      paginaActual: 1,
      logroSeleccionado: null
    };
  }
}
