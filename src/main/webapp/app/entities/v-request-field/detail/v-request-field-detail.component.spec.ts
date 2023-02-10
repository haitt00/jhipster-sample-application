import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VRequestFieldDetailComponent } from './v-request-field-detail.component';

describe('VRequestField Management Detail Component', () => {
  let comp: VRequestFieldDetailComponent;
  let fixture: ComponentFixture<VRequestFieldDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VRequestFieldDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vRequestField: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VRequestFieldDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VRequestFieldDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vRequestField on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vRequestField).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
