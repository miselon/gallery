import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationCompleteViewComponent } from './registration-complete-view.component';

describe('RegistrationCompleteViewComponent', () => {
  let component: RegistrationCompleteViewComponent;
  let fixture: ComponentFixture<RegistrationCompleteViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegistrationCompleteViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationCompleteViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
