import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceptionTravauxComponent } from './reception-travaux.component';

describe('ReceptionTravauxComponent', () => {
  let component: ReceptionTravauxComponent;
  let fixture: ComponentFixture<ReceptionTravauxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReceptionTravauxComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReceptionTravauxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
