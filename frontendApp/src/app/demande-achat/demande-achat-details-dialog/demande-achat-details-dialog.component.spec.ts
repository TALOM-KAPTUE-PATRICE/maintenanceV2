import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandeAchatDetailsDialogComponent } from './demande-achat-details-dialog.component';

describe('DemandeAchatDetailsDialogComponent', () => {
  let component: DemandeAchatDetailsDialogComponent;
  let fixture: ComponentFixture<DemandeAchatDetailsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DemandeAchatDetailsDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DemandeAchatDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
