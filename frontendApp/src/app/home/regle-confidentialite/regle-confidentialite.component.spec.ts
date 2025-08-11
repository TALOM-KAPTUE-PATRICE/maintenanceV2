import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegleConfidentialiteComponent } from './regle-confidentialite.component';

describe('RegleConfidentialiteComponent', () => {
  let component: RegleConfidentialiteComponent;
  let fixture: ComponentFixture<RegleConfidentialiteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegleConfidentialiteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegleConfidentialiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
