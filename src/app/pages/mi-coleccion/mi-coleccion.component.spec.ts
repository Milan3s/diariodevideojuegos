import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MiColeccionComponent } from './mi-coleccion.component';

describe('MiColeccionComponent', () => {
  let component: MiColeccionComponent;
  let fixture: ComponentFixture<MiColeccionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MiColeccionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MiColeccionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
