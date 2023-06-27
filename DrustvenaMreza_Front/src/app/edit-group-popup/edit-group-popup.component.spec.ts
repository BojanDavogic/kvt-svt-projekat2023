import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditGroupPopupComponent } from './edit-group-popup.component';

describe('EditGroupPopupComponent', () => {
  let component: EditGroupPopupComponent;
  let fixture: ComponentFixture<EditGroupPopupComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditGroupPopupComponent]
    });
    fixture = TestBed.createComponent(EditGroupPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
