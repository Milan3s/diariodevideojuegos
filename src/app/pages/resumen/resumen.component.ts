import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // ✅ Necesario para pipes como 'date'
import { HttpClientModule } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-resumen',
  standalone: true,
  imports: [CommonModule, HttpClientModule], // ✅ Aquí importa el módulo común
  templateUrl: './resumen.component.html',
  styleUrls: ['./resumen.component.css']
})
export class ResumenComponent implements OnInit {
  resumen: any = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get('http://localhost:3000/api/resumen').subscribe({
      next: (data) => this.resumen = data,
      error: (err) => console.error('Error al cargar resumen:', err)
    });
  }
}
