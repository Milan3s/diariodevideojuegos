import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface Consola {
  id_consola: number;
  nombre: string;
  abreviatura?: string;
  anio?: number;
  fabricante?: string;
  generacion?: string;
  region?: string;
  tipo?: string;
  procesador?: string;
  memoria?: string;
  almacenamiento?: string;
  frecuencia_mhz?: number;
  chip?: boolean;
  caracteristicas?: string;
  fecha_lanzamiento?: string;
  imagen?: string;
  RutaImagen?: string;
  video?: string;
  RutaVideo?: string;
  id_estado?: number;
  original?: boolean;
  modificada?: boolean;
  caja?: boolean;
  precintada?: boolean;
  hz?: string;
  estado_nombre?: string;
  cantidad_juegos?: number;
  seleccionado?: boolean; // para la selección múltiple
}

@Injectable({ providedIn: 'root' })
export class ConsolasService {
  private baseUrl = 'http://localhost:3000/api/consolas';

  constructor(private http: HttpClient) {}

  obtenerConsolas(): Observable<Consola[]> {
    return this.http.get<Consola[]>(this.baseUrl).pipe(
      catchError(this.manejarError)
    );
  }

  getEstadosConsola(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/estados`).pipe(
      catchError(this.manejarError)
    );
  }

  agregarConsola(data: FormData): Observable<any> {
    return this.http.post(this.baseUrl, data).pipe(
      catchError(this.manejarError)
    );
  }

  actualizarConsola(id: number, data: FormData): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, data).pipe(
      catchError(this.manejarError)
    );
  }

  eliminarConsola(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`).pipe(
      catchError(this.manejarError)
    );
  }

  eliminarVariasConsolas(ids: number[]): Observable<any> {
    return this.http.post(`${this.baseUrl}/eliminar-multiples`, { ids }).pipe(
      catchError(this.manejarError)
    );
  }

  private manejarError(error: HttpErrorResponse): Observable<never> {
    console.error('Error en ConsolasService:', error);
    return throwError(() => new Error('Error en la solicitud HTTP'));
  }
}
