import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PeopleGroupListComponent } from './people-group-list.component';

describe('PeopleGroupListComponent', () => {
  let component: PeopleGroupListComponent;
  let fixture: ComponentFixture<PeopleGroupListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PeopleGroupListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PeopleGroupListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
