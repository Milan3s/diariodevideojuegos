import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CatalogosDeJuegosPorConsolaComponent } from './catalogos-de-juegos-por-consola.component';

describe('CatalogosDeJuegosPorConsolaComponent', () => {
  let component: CatalogosDeJuegosPorConsolaComponent;
  let fixture: ComponentFixture<CatalogosDeJuegosPorConsolaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CatalogosDeJuegosPorConsolaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CatalogosDeJuegosPorConsolaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
