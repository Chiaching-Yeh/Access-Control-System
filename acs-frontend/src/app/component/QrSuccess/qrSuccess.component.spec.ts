import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QrSuccessComponent } from './qrSuccess.component';

describe('QrSuccessComponent', () => {
  let component: QrSuccessComponent;
  let fixture: ComponentFixture<QrSuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QrSuccessComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QrSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
