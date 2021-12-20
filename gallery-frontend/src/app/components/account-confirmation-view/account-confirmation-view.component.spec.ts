import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountConfirmationViewComponent } from './account-confirmation-view.component';

describe('AccountConfirmationViewComponent', () => {
  let component: AccountConfirmationViewComponent;
  let fixture: ComponentFixture<AccountConfirmationViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccountConfirmationViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountConfirmationViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
