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
}

@Injectable({ providedIn: 'root' })
export class ModeradoresService {
  private baseUrl = 'http://localhost:3000/api/moderadores';

  constructor(private http: HttpClient) {}

  obtenerModeradores(): Observable<Moderador[]> {
    return this.http.get<Moderador[]>(this.baseUrl).pipe(
      catchError(this.manejarError)
    );
  }

  private manejarError(error: HttpErrorResponse): Observable<never> {
    console.error('Error en ModeradoresService:', error);
    return throwError(() => new Error('Error en la solicitud HTTP'));
  }
}
