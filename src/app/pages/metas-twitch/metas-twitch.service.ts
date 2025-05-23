import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface MetaTwitch {
  id_meta: number;
  descripcion: string;
  meta: number;
  actual: number;
  fecha_inicio: string;
  fecha_fin: string;
  fecha_registro: string;
  id_estado_metas: number;
  estado_nombre: string;
  seleccionado?: boolean;
}

export interface EstadoMeta {
  id: number;
  nombre_estado: string;
}

@Injectable({ providedIn: 'root' })
export class MetasTwitchService {
  private baseUrl = 'http://localhost:3000/api/metas-twitch';

  constructor(private http: HttpClient) {}

  getMetas(): Observable<MetaTwitch[]> {
    return this.http.get<MetaTwitch[]>(this.baseUrl).pipe(
      catchError(this.handleError)
    );
  }

  actualizarMeta(id: number, datos: Partial<MetaTwitch>): Observable<MetaTwitch> {
    return this.http.put<MetaTwitch>(`${this.baseUrl}/${id}`, datos).pipe(
      catchError(this.handleError)
    );
  }

  agregarMeta(datos: Partial<MetaTwitch>): Observable<MetaTwitch> {
    return this.http.post<MetaTwitch>(this.baseUrl, datos).pipe(
      catchError(this.handleError)
    );
  }

  eliminarMultiples(ids: number[]): Observable<{ success: boolean }> {
    return this.http.post<{ success: boolean }>(`${this.baseUrl}/eliminar-multiples`, { ids }).pipe(
      catchError(this.handleError)
    );
  }

  getEstados(): Observable<EstadoMeta[]> {
    return this.http.get<EstadoMeta[]>(`${this.baseUrl}/estados`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Error desconocido';

    if (error.error instanceof ErrorEvent) {
      // Error del cliente (no conexión, problema de red, etc.)
      errorMessage = `Error del cliente: ${error.error.message}`;
    } else {
      // Error del servidor
      errorMessage = `Error en la solicitud HTTP: ${error.status}, ${error.message}`;
    }

    console.error('❌ Error en MetasTwitchService:', errorMessage);

    // Puedes emitir un error con un mensaje más detallado aquí si lo deseas.
    return throwError(() => new Error(errorMessage));
  }
}
