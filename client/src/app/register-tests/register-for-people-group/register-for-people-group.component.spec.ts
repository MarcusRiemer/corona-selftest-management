import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterForPeopleGroupComponent } from './register-for-people-group.component';

describe('RegisterForPeopleGroupComponent', () => {
  let component: RegisterForPeopleGroupComponent;
  let fixture: ComponentFixture<RegisterForPeopleGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterForPeopleGroupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterForPeopleGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
