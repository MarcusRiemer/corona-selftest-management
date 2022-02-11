import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExemptionStatusComponent } from './exemption-status.component';

describe('ExemptionStatusComponent', () => {
  let component: ExemptionStatusComponent;
  let fixture: ComponentFixture<ExemptionStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExemptionStatusComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExemptionStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
