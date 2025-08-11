import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BonCommandeComponent } from './bon-commande.component';

describe('BonCommandeComponent', () => {
  let component: BonCommandeComponent;
  let fixture: ComponentFixture<BonCommandeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BonCommandeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BonCommandeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
