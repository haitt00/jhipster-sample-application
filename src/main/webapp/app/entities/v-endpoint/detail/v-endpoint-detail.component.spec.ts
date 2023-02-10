import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VEndpointDetailComponent } from './v-endpoint-detail.component';

describe('VEndpoint Management Detail Component', () => {
  let comp: VEndpointDetailComponent;
  let fixture: ComponentFixture<VEndpointDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VEndpointDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vEndpoint: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VEndpointDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VEndpointDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vEndpoint on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vEndpoint).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
