import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-moderadores',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './moderadores.component.html',
  styleUrls: ['./moderadores.component.css']
})
export class ModeradoresComponent {
  moderadores: any[] = [];
  moderadorSeleccionado: any = null;

  seleccionarModerador(m: any) {
    this.moderadorSeleccionado = m;
  }

  limpiarSeleccion() {
    this.moderadorSeleccionado = null;
  }
}
