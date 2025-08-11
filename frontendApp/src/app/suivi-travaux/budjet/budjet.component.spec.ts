import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BudjetComponent } from './budjet.component';

describe('BudjetComponent', () => {
  let component: BudjetComponent;
  let fixture: ComponentFixture<BudjetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BudjetComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BudjetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
