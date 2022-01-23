import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectTestResultComponent } from './select-test-result.component';

describe('SelectTestResultComponent', () => {
  let component: SelectTestResultComponent;
  let fixture: ComponentFixture<SelectTestResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SelectTestResultComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectTestResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
