import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BonCommandeClientListComponent } from './bon-commande-client-list.component';

describe('BonCommandeClientListComponent', () => {
  let component: BonCommandeClientListComponent;
  let fixture: ComponentFixture<BonCommandeClientListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BonCommandeClientListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BonCommandeClientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
