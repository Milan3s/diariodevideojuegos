import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface Moderador {
  id_moderador: number;
  nombre: string;
  email: string;
  fecha_alta?: string;
  fecha_baja?: string;
  id_estado?: number;
  estado_nombre?: string;
  seleccionado?: boolean;
}

@Injectable({ providedIn: 'root' })
export class ModeradoresService {
  private baseUrl = 'http://localhost:3000/api/moderadores';
  private estadosUrl = 'http://localhost:3000/api/estados?tipo=moderador';

  constructor(private http: HttpClient) {}

  obtenerModeradores(): Observable<Moderador[]> {
    return this.http.get<Moderador[]>(this.baseUrl).pipe(
      catchError(this.manejarError)
    );
  }

  obtenerEstados(): Observable<any[]> {
    return this.http.get<any[]>(this.estadosUrl).pipe(
      catchError(this.manejarError)
    );
  }

  agregarModerador(data: Partial<Moderador>): Observable<any> {
    return this.http.post(this.baseUrl, data).pipe(
      catchError(this.manejarError)
    );
  }

  editarModerador(id: number, data: Partial<Moderador>): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, data).pipe(
      catchError(this.manejarError)
    );
  }

  eliminarModeradores(ids: number[]): Observable<any> {
    return this.http.post(`${this.baseUrl}/eliminar`, { ids }).pipe(
      catchError(this.manejarError)
    );
  }

  darDeAlta(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/alta/${id}`, {}).pipe(
      catchError(this.manejarError)
    );
  }

  darDeBaja(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/baja/${id}`, {}).pipe(
      catchError(this.manejarError)
    );
  }

  readmitir(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/readmitir/${id}`, {}).pipe(
      catchError(this.manejarError)
    );
  }

  private manejarError(error: HttpErrorResponse): Observable<never> {
    console.error('❌ Error en ModeradoresService:', error);
    return throwError(() => new Error('Error en la solicitud HTTP'));
  }
}
