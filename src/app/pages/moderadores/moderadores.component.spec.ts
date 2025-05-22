import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModeradoresComponent } from './moderadores.component';

describe('ModeradoresComponent', () => {
  let component: ModeradoresComponent;
  let fixture: ComponentFixture<ModeradoresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModeradoresComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModeradoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
