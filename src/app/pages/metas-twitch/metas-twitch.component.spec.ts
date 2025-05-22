import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetasTwitchComponent } from './metas-twitch.component';

describe('MetasTwitchComponent', () => {
  let component: MetasTwitchComponent;
  let fixture: ComponentFixture<MetasTwitchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MetasTwitchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MetasTwitchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
