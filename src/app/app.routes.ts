import { Routes } from '@angular/router';

import { ResumenComponent } from './pages/resumen/resumen.component';
import { MetasTwitchComponent } from './pages/metas-twitch/metas-twitch.component';
import { MetasEspecificasComponent } from './pages/metas-especificas/metas-especificas.component';
import { MejorasDelCanalComponent } from './pages/mejoras-del-canal/mejoras-del-canal.component';

import { ConsolasComponent } from './pages/consolas/consolas.component';
import { JuegosComponent } from './pages/juegos/juegos.component';
import { MiColeccionComponent } from './pages/mi-coleccion/mi-coleccion.component';
import { CatalogosDeJuegosPorConsolaComponent } from './pages/catalogos-de-juegos-por-consola/catalogos-de-juegos-por-consola.component';

import { LogrosComponent } from './pages/logros/logros.component';
import { ModeradoresComponent } from './pages/moderadores/moderadores.component';
import { DatosAuxiliaresComponent } from './pages/datos-auxiliares/datos-auxiliares.component';
import { EventosComponent } from './pages/eventos/eventos.component';
import { EventosExtensiblesComponent } from './pages/eventos-extensibles/eventos-extensibles.component';

export const routes: Routes = [
  { path: 'resumen', component: ResumenComponent },
  { path: 'metas-twitch', component: MetasTwitchComponent },
  { path: 'metas-especificas', component: MetasEspecificasComponent },
  { path: 'mejoras-del-canal', component: MejorasDelCanalComponent },

  { path: 'consolas', component: ConsolasComponent },

  { path: 'juegos', component: JuegosComponent },
  { path: 'mi-coleccion', component: MiColeccionComponent },
  { path: 'catalogos-de-juegos-por-consola', component: CatalogosDeJuegosPorConsolaComponent },

  { path: 'logros', component: LogrosComponent },
  { path: 'moderadores', component: ModeradoresComponent },
  { path: 'datos-auxiliares', component: DatosAuxiliaresComponent },

  { path: 'eventos', component: EventosComponent },
  { path: 'eventos-extensibles', component: EventosExtensiblesComponent },

  // Redirección por defecto
  { path: '', redirectTo: 'moderadores', pathMatch: 'full' }
];
