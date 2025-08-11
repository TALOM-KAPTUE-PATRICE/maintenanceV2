import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeDaComponent } from './liste-da.component';

describe('ListeDaComponent', () => {
  let component: ListeDaComponent;
  let fixture: ComponentFixture<ListeDaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListeDaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListeDaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
