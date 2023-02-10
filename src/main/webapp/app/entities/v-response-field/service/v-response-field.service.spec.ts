import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVResponseField } from '../v-response-field.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../v-response-field.test-samples';

import { VResponseFieldService } from './v-response-field.service';

const requireRestSample: IVResponseField = {
  ...sampleWithRequiredData,
};

describe('VResponseField Service', () => {
  let service: VResponseFieldService;
  let httpMock: HttpTestingController;
  let expectedResult: IVResponseField | IVResponseField[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VResponseFieldService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a VResponseField', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const vResponseField = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vResponseField).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VResponseField', () => {
      const vResponseField = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vResponseField).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VResponseField', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VResponseField', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VResponseField', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVResponseFieldToCollectionIfMissing', () => {
      it('should add a VResponseField to an empty array', () => {
        const vResponseField: IVResponseField = sampleWithRequiredData;
        expectedResult = service.addVResponseFieldToCollectionIfMissing([], vResponseField);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vResponseField);
      });

      it('should not add a VResponseField to an array that contains it', () => {
        const vResponseField: IVResponseField = sampleWithRequiredData;
        const vResponseFieldCollection: IVResponseField[] = [
          {
            ...vResponseField,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVResponseFieldToCollectionIfMissing(vResponseFieldCollection, vResponseField);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VResponseField to an array that doesn't contain it", () => {
        const vResponseField: IVResponseField = sampleWithRequiredData;
        const vResponseFieldCollection: IVResponseField[] = [sampleWithPartialData];
        expectedResult = service.addVResponseFieldToCollectionIfMissing(vResponseFieldCollection, vResponseField);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vResponseField);
      });

      it('should add only unique VResponseField to an array', () => {
        const vResponseFieldArray: IVResponseField[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vResponseFieldCollection: IVResponseField[] = [sampleWithRequiredData];
        expectedResult = service.addVResponseFieldToCollectionIfMissing(vResponseFieldCollection, ...vResponseFieldArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vResponseField: IVResponseField = sampleWithRequiredData;
        const vResponseField2: IVResponseField = sampleWithPartialData;
        expectedResult = service.addVResponseFieldToCollectionIfMissing([], vResponseField, vResponseField2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vResponseField);
        expect(expectedResult).toContain(vResponseField2);
      });

      it('should accept null and undefined values', () => {
        const vResponseField: IVResponseField = sampleWithRequiredData;
        expectedResult = service.addVResponseFieldToCollectionIfMissing([], null, vResponseField, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vResponseField);
      });

      it('should return initial array if no VResponseField is added', () => {
        const vResponseFieldCollection: IVResponseField[] = [sampleWithRequiredData];
        expectedResult = service.addVResponseFieldToCollectionIfMissing(vResponseFieldCollection, undefined, null);
        expect(expectedResult).toEqual(vResponseFieldCollection);
      });
    });

    describe('compareVResponseField', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVResponseField(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVResponseField(entity1, entity2);
        const compareResult2 = service.compareVResponseField(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVResponseField(entity1, entity2);
        const compareResult2 = service.compareVResponseField(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVResponseField(entity1, entity2);
        const compareResult2 = service.compareVResponseField(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
