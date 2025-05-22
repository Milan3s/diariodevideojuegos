import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DatosAuxiliaresComponent } from './datos-auxiliares.component';

describe('DatosAuxiliaresComponent', () => {
  let component: DatosAuxiliaresComponent;
  let fixture: ComponentFixture<DatosAuxiliaresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DatosAuxiliaresComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DatosAuxiliaresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
