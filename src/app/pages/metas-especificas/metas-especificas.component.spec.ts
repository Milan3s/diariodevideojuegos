import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetasEspecificasComponent } from './metas-especificas.component';

describe('MetasEspecificasComponent', () => {
  let component: MetasEspecificasComponent;
  let fixture: ComponentFixture<MetasEspecificasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MetasEspecificasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MetasEspecificasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
