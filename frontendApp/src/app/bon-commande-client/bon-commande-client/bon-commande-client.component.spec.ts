import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BonCommandeClientComponent } from './bon-commande-client.component';

describe('BonCommandeClientComponent', () => {
  let component: BonCommandeClientComponent;
  let fixture: ComponentFixture<BonCommandeClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BonCommandeClientComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BonCommandeClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
