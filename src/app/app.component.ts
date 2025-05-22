import { Component } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';

interface MenuItem {
  label: string;
  route?: string;
  children?: MenuItem[];
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    NgIf,
    NgFor
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'mi-diario-de-juegos';
  openSubmenu: string | null = null;

  toggleSubmenu(label: string) {
    this.openSubmenu = this.openSubmenu === label ? null : label;
  }

  menu: MenuItem[] = [
    {
      label: 'Inicio',
      children: [
        { label: 'Resumen del Día', route: '/resumen' },
        { label: 'Metas Twitch', route: '/metas-twitch' },
        { label: 'Metas Específicas', route: '/metas-especificas' },
        { label: 'Mejoras del Canal', route: '/mejoras-del-canal' }
      ]
    },
    { label: 'Consolas', route: '/consolas' },
    {
      label: 'Juegos',
      children: [
        { label: 'Juegos en general', route: '/juegos' },
        { label: 'Mi colección', route: '/mi-coleccion' },
        { label: 'Catálogos por consola', route: '/catalogos-de-juegos-por-consola' }
      ]
    },
    { label: 'Logros', route: '/logros' },
    { label: 'Moderador', route: '/moderadores' },
    { label: 'Datos auxiliares', route: '/datos-auxiliares' },
    {
      label: 'Eventos',
      children: [
        { label: 'Ver Eventos', route: '/eventos' },
        { label: 'Eventos Extensibles', route: '/eventos-extensibles' }
      ]
    }
  ];
}
