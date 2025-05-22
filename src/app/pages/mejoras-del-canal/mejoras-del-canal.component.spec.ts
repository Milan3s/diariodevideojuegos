import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MejorasDelCanalComponent } from './mejoras-del-canal.component';

describe('MejorasDelCanalComponent', () => {
  let component: MejorasDelCanalComponent;
  let fixture: ComponentFixture<MejorasDelCanalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MejorasDelCanalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MejorasDelCanalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
