import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface ResumenDelDia {
  fecha: string;
  totalConsolas: number;
}

@Injectable({ providedIn: 'root' })
export class ResumenService {
  private baseUrl = 'http://localhost:3000/api/resumen';

  constructor(private http: HttpClient) {}

  obtenerResumen(): Observable<ResumenDelDia> {
    return this.http.get<ResumenDelDia>(this.baseUrl).pipe(
      catchError(this.manejarError)
    );
  }

  private manejarError(error: HttpErrorResponse): Observable<never> {
    console.error('Error en ResumenService:', error);
    return throwError(() => new Error('Error en la solicitud de resumen'));
  }
}
