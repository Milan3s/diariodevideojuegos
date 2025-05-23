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

  getEstados(): Observable<EstadoMeta[]> {
    return this.http.get<EstadoMeta[]>(`${this.baseUrl}/estados`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('❌ Error en MetasTwitchService:', error);
    return throwError(() => new Error('Error en la solicitud HTTP de metas'));
  }
}
