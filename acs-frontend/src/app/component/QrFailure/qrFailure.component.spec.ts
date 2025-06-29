import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QrFailureComponent } from './qrFailure.component';

describe('QrFailureComponent', () => {
  let component: QrFailureComponent;
  let fixture: ComponentFixture<QrFailureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QrFailureComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QrFailureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
