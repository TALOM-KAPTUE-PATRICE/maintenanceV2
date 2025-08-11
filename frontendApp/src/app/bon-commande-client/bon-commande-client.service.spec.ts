import { TestBed } from '@angular/core/testing';

import { BonCommandeClientService } from './bon-commande-client.service';

describe('BonCommandeClientService', () => {
  let service: BonCommandeClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BonCommandeClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
