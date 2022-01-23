import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectTestOriginComponent } from './select-test-origin.component';

describe('SelectTestStateComponent', () => {
  let component: SelectTestOriginComponent;
  let fixture: ComponentFixture<SelectTestOriginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SelectTestOriginComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectTestOriginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
