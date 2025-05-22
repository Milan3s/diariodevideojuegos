import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventosExtensiblesComponent } from './eventos-extensibles.component';

describe('EventosExtensiblesComponent', () => {
  let component: EventosExtensiblesComponent;
  let fixture: ComponentFixture<EventosExtensiblesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventosExtensiblesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventosExtensiblesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
