import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SpreadsheetModule } from './spreadsheet.module';
import { Clipboard, Edit, KeyboardNavigation, KeyboardShortcut, Selection, ContextMenu, FormulaBar, Ribbon, Save, Open, SheetTabs, DataBind, CellFormat, NumberFormat, Formula } from '@syncfusion/ej2-spreadsheet';
import * as i0 from "@angular/core";
export const ClipboardService = { provide: 'SpreadsheetClipboard', useValue: Clipboard };
export const EditService = { provide: 'SpreadsheetEdit', useValue: Edit };
export const KeyboardNavigationService = { provide: 'SpreadsheetKeyboardNavigation', useValue: KeyboardNavigation };
export const KeyboardShortcutService = { provide: 'SpreadsheetKeyboardShortcut', useValue: KeyboardShortcut };
export const SelectionService = { provide: 'SpreadsheetSelection', useValue: Selection };
export const ContextMenuService = { provide: 'SpreadsheetContextMenu', useValue: ContextMenu };
export const FormulaBarService = { provide: 'SpreadsheetFormulaBar', useValue: FormulaBar };
export const RibbonService = { provide: 'SpreadsheetRibbon', useValue: Ribbon };
export const SaveService = { provide: 'SpreadsheetSave', useValue: Save };
export const OpenService = { provide: 'SpreadsheetOpen', useValue: Open };
export const SheetTabsService = { provide: 'SpreadsheetSheetTabs', useValue: SheetTabs };
export const DataBindService = { provide: 'SpreadsheetDataBind', useValue: DataBind };
export const CellFormatService = { provide: 'SpreadsheetCellFormat', useValue: CellFormat };
export const NumberFormatService = { provide: 'SpreadsheetNumberFormat', useValue: NumberFormat };
export const FormulaService = { provide: 'SpreadsheetFormula', useValue: Formula };
/**
 * NgModule definition for the Spreadsheet component with providers.
 */
export class SpreadsheetAllModule {
}
SpreadsheetAllModule.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: SpreadsheetAllModule, deps: [], target: i0.ɵɵFactoryTarget.NgModule });
SpreadsheetAllModule.ɵmod = i0.ɵɵngDeclareNgModule({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: SpreadsheetAllModule, imports: [CommonModule, SpreadsheetModule], exports: [SpreadsheetModule] });
SpreadsheetAllModule.ɵinj = i0.ɵɵngDeclareInjector({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: SpreadsheetAllModule, providers: [
        ClipboardService,
        EditService,
        KeyboardNavigationService,
        KeyboardShortcutService,
        SelectionService,
        ContextMenuService,
        FormulaBarService,
        RibbonService,
        SaveService,
        OpenService,
        SheetTabsService,
        DataBindService,
        CellFormatService,
        NumberFormatService,
        FormulaService
    ], imports: [[CommonModule, SpreadsheetModule], SpreadsheetModule] });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: SpreadsheetAllModule, decorators: [{
            type: NgModule,
            args: [{
                    imports: [CommonModule, SpreadsheetModule],
                    exports: [
                        SpreadsheetModule
                    ],
                    providers: [
                        ClipboardService,
                        EditService,
                        KeyboardNavigationService,
                        KeyboardShortcutService,
                        SelectionService,
                        ContextMenuService,
                        FormulaBarService,
                        RibbonService,
                        SaveService,
                        OpenService,
                        SheetTabsService,
                        DataBindService,
                        CellFormatService,
                        NumberFormatService,
                        FormulaService
                    ]
                }]
        }] });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoic3ByZWFkc2hlZXQtYWxsLm1vZHVsZS5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uL3NyYy9zcHJlYWRzaGVldC9zcHJlYWRzaGVldC1hbGwubW9kdWxlLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBLE9BQU8sRUFBRSxRQUFRLEVBQWlCLE1BQU0sZUFBZSxDQUFDO0FBQ3hELE9BQU8sRUFBRSxZQUFZLEVBQUUsTUFBTSxpQkFBaUIsQ0FBQztBQVcvQyxPQUFPLEVBQUUsaUJBQWlCLEVBQUUsTUFBTSxzQkFBc0IsQ0FBQztBQUN6RCxPQUFPLEVBQUMsU0FBUyxFQUFFLElBQUksRUFBRSxrQkFBa0IsRUFBRSxnQkFBZ0IsRUFBRSxTQUFTLEVBQUUsV0FBVyxFQUFFLFVBQVUsRUFBRSxNQUFNLEVBQUUsSUFBSSxFQUFFLElBQUksRUFBRSxTQUFTLEVBQUUsUUFBUSxFQUFFLFVBQVUsRUFBRSxZQUFZLEVBQUUsT0FBTyxFQUFDLE1BQU0sNkJBQTZCLENBQUE7O0FBR2pOLE1BQU0sQ0FBQyxNQUFNLGdCQUFnQixHQUFrQixFQUFFLE9BQU8sRUFBRSxzQkFBc0IsRUFBRSxRQUFRLEVBQUUsU0FBUyxFQUFDLENBQUM7QUFDdkcsTUFBTSxDQUFDLE1BQU0sV0FBVyxHQUFrQixFQUFFLE9BQU8sRUFBRSxpQkFBaUIsRUFBRSxRQUFRLEVBQUUsSUFBSSxFQUFDLENBQUM7QUFDeEYsTUFBTSxDQUFDLE1BQU0seUJBQXlCLEdBQWtCLEVBQUUsT0FBTyxFQUFFLCtCQUErQixFQUFFLFFBQVEsRUFBRSxrQkFBa0IsRUFBQyxDQUFDO0FBQ2xJLE1BQU0sQ0FBQyxNQUFNLHVCQUF1QixHQUFrQixFQUFFLE9BQU8sRUFBRSw2QkFBNkIsRUFBRSxRQUFRLEVBQUUsZ0JBQWdCLEVBQUMsQ0FBQztBQUM1SCxNQUFNLENBQUMsTUFBTSxnQkFBZ0IsR0FBa0IsRUFBRSxPQUFPLEVBQUUsc0JBQXNCLEVBQUUsUUFBUSxFQUFFLFNBQVMsRUFBQyxDQUFDO0FBQ3ZHLE1BQU0sQ0FBQyxNQUFNLGtCQUFrQixHQUFrQixFQUFFLE9BQU8sRUFBRSx3QkFBd0IsRUFBRSxRQUFRLEVBQUUsV0FBVyxFQUFDLENBQUM7QUFDN0csTUFBTSxDQUFDLE1BQU0saUJBQWlCLEdBQWtCLEVBQUUsT0FBTyxFQUFFLHVCQUF1QixFQUFFLFFBQVEsRUFBRSxVQUFVLEVBQUMsQ0FBQztBQUMxRyxNQUFNLENBQUMsTUFBTSxhQUFhLEdBQWtCLEVBQUUsT0FBTyxFQUFFLG1CQUFtQixFQUFFLFFBQVEsRUFBRSxNQUFNLEVBQUMsQ0FBQztBQUM5RixNQUFNLENBQUMsTUFBTSxXQUFXLEdBQWtCLEVBQUUsT0FBTyxFQUFFLGlCQUFpQixFQUFFLFFBQVEsRUFBRSxJQUFJLEVBQUMsQ0FBQztBQUN4RixNQUFNLENBQUMsTUFBTSxXQUFXLEdBQWtCLEVBQUUsT0FBTyxFQUFFLGlCQUFpQixFQUFFLFFBQVEsRUFBRSxJQUFJLEVBQUMsQ0FBQztBQUN4RixNQUFNLENBQUMsTUFBTSxnQkFBZ0IsR0FBa0IsRUFBRSxPQUFPLEVBQUUsc0JBQXNCLEVBQUUsUUFBUSxFQUFFLFNBQVMsRUFBQyxDQUFDO0FBQ3ZHLE1BQU0sQ0FBQyxNQUFNLGVBQWUsR0FBa0IsRUFBRSxPQUFPLEVBQUUscUJBQXFCLEVBQUUsUUFBUSxFQUFFLFFBQVEsRUFBQyxDQUFDO0FBQ3BHLE1BQU0sQ0FBQyxNQUFNLGlCQUFpQixHQUFrQixFQUFFLE9BQU8sRUFBRSx1QkFBdUIsRUFBRSxRQUFRLEVBQUUsVUFBVSxFQUFDLENBQUM7QUFDMUcsTUFBTSxDQUFDLE1BQU0sbUJBQW1CLEdBQWtCLEVBQUUsT0FBTyxFQUFFLHlCQUF5QixFQUFFLFFBQVEsRUFBRSxZQUFZLEVBQUMsQ0FBQztBQUNoSCxNQUFNLENBQUMsTUFBTSxjQUFjLEdBQWtCLEVBQUUsT0FBTyxFQUFFLG9CQUFvQixFQUFFLFFBQVEsRUFBRSxPQUFPLEVBQUMsQ0FBQztBQUVqRzs7R0FFRztBQXdCSCxNQUFNLE9BQU8sb0JBQW9COztpSEFBcEIsb0JBQW9CO2tIQUFwQixvQkFBb0IsWUF0Qm5CLFlBQVksRUFBRSxpQkFBaUIsYUFFckMsaUJBQWlCO2tIQW9CWixvQkFBb0IsYUFsQm5CO1FBQ04sZ0JBQWdCO1FBQ2hCLFdBQVc7UUFDWCx5QkFBeUI7UUFDekIsdUJBQXVCO1FBQ3ZCLGdCQUFnQjtRQUNoQixrQkFBa0I7UUFDbEIsaUJBQWlCO1FBQ2pCLGFBQWE7UUFDYixXQUFXO1FBQ1gsV0FBVztRQUNYLGdCQUFnQjtRQUNoQixlQUFlO1FBQ2YsaUJBQWlCO1FBQ2pCLG1CQUFtQjtRQUNuQixjQUFjO0tBQ2pCLFlBcEJRLENBQUMsWUFBWSxFQUFFLGlCQUFpQixDQUFDLEVBRXRDLGlCQUFpQjsyRkFvQlosb0JBQW9CO2tCQXZCaEMsUUFBUTttQkFBQztvQkFDTixPQUFPLEVBQUUsQ0FBQyxZQUFZLEVBQUUsaUJBQWlCLENBQUM7b0JBQzFDLE9BQU8sRUFBRTt3QkFDTCxpQkFBaUI7cUJBQ3BCO29CQUNELFNBQVMsRUFBQzt3QkFDTixnQkFBZ0I7d0JBQ2hCLFdBQVc7d0JBQ1gseUJBQXlCO3dCQUN6Qix1QkFBdUI7d0JBQ3ZCLGdCQUFnQjt3QkFDaEIsa0JBQWtCO3dCQUNsQixpQkFBaUI7d0JBQ2pCLGFBQWE7d0JBQ2IsV0FBVzt3QkFDWCxXQUFXO3dCQUNYLGdCQUFnQjt3QkFDaEIsZUFBZTt3QkFDZixpQkFBaUI7d0JBQ2pCLG1CQUFtQjt3QkFDbkIsY0FBYztxQkFDakI7aUJBQ0oiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBOZ01vZHVsZSwgVmFsdWVQcm92aWRlciB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgQ29tbW9uTW9kdWxlIH0gZnJvbSAnQGFuZ3VsYXIvY29tbW9uJztcbmltcG9ydCB7IEltYWdlRGlyZWN0aXZlLCBJbWFnZXNEaXJlY3RpdmUgfSBmcm9tICcuL2ltYWdlLmRpcmVjdGl2ZSc7XG5pbXBvcnQgeyBDaGFydERpcmVjdGl2ZSwgQ2hhcnRzRGlyZWN0aXZlIH0gZnJvbSAnLi9jaGFydC5kaXJlY3RpdmUnO1xuaW1wb3J0IHsgQ2VsbERpcmVjdGl2ZSwgQ2VsbHNEaXJlY3RpdmUgfSBmcm9tICcuL2NlbGxzLmRpcmVjdGl2ZSc7XG5pbXBvcnQgeyBSb3dEaXJlY3RpdmUsIFJvd3NEaXJlY3RpdmUgfSBmcm9tICcuL3Jvd3MuZGlyZWN0aXZlJztcbmltcG9ydCB7IENvbHVtbkRpcmVjdGl2ZSwgQ29sdW1uc0RpcmVjdGl2ZSB9IGZyb20gJy4vY29sdW1ucy5kaXJlY3RpdmUnO1xuaW1wb3J0IHsgUmFuZ2VEaXJlY3RpdmUsIFJhbmdlc0RpcmVjdGl2ZSB9IGZyb20gJy4vcmFuZ2VzLmRpcmVjdGl2ZSc7XG5pbXBvcnQgeyBDb25kaXRpb25hbEZvcm1hdERpcmVjdGl2ZSwgQ29uZGl0aW9uYWxGb3JtYXRzRGlyZWN0aXZlIH0gZnJvbSAnLi9jb25kaXRpb25hbGZvcm1hdHMuZGlyZWN0aXZlJztcbmltcG9ydCB7IFNoZWV0RGlyZWN0aXZlLCBTaGVldHNEaXJlY3RpdmUgfSBmcm9tICcuL3NoZWV0cy5kaXJlY3RpdmUnO1xuaW1wb3J0IHsgRGVmaW5lZE5hbWVEaXJlY3RpdmUsIERlZmluZWROYW1lc0RpcmVjdGl2ZSB9IGZyb20gJy4vZGVmaW5lZG5hbWVzLmRpcmVjdGl2ZSc7XG5pbXBvcnQgeyBTcHJlYWRzaGVldENvbXBvbmVudCB9IGZyb20gJy4vc3ByZWFkc2hlZXQuY29tcG9uZW50JztcbmltcG9ydCB7IFNwcmVhZHNoZWV0TW9kdWxlIH0gZnJvbSAnLi9zcHJlYWRzaGVldC5tb2R1bGUnO1xuaW1wb3J0IHtDbGlwYm9hcmQsIEVkaXQsIEtleWJvYXJkTmF2aWdhdGlvbiwgS2V5Ym9hcmRTaG9ydGN1dCwgU2VsZWN0aW9uLCBDb250ZXh0TWVudSwgRm9ybXVsYUJhciwgUmliYm9uLCBTYXZlLCBPcGVuLCBTaGVldFRhYnMsIERhdGFCaW5kLCBDZWxsRm9ybWF0LCBOdW1iZXJGb3JtYXQsIEZvcm11bGF9IGZyb20gJ0BzeW5jZnVzaW9uL2VqMi1zcHJlYWRzaGVldCdcblxuXG5leHBvcnQgY29uc3QgQ2xpcGJvYXJkU2VydmljZTogVmFsdWVQcm92aWRlciA9IHsgcHJvdmlkZTogJ1NwcmVhZHNoZWV0Q2xpcGJvYXJkJywgdXNlVmFsdWU6IENsaXBib2FyZH07XG5leHBvcnQgY29uc3QgRWRpdFNlcnZpY2U6IFZhbHVlUHJvdmlkZXIgPSB7IHByb3ZpZGU6ICdTcHJlYWRzaGVldEVkaXQnLCB1c2VWYWx1ZTogRWRpdH07XG5leHBvcnQgY29uc3QgS2V5Ym9hcmROYXZpZ2F0aW9uU2VydmljZTogVmFsdWVQcm92aWRlciA9IHsgcHJvdmlkZTogJ1NwcmVhZHNoZWV0S2V5Ym9hcmROYXZpZ2F0aW9uJywgdXNlVmFsdWU6IEtleWJvYXJkTmF2aWdhdGlvbn07XG5leHBvcnQgY29uc3QgS2V5Ym9hcmRTaG9ydGN1dFNlcnZpY2U6IFZhbHVlUHJvdmlkZXIgPSB7IHByb3ZpZGU6ICdTcHJlYWRzaGVldEtleWJvYXJkU2hvcnRjdXQnLCB1c2VWYWx1ZTogS2V5Ym9hcmRTaG9ydGN1dH07XG5leHBvcnQgY29uc3QgU2VsZWN0aW9uU2VydmljZTogVmFsdWVQcm92aWRlciA9IHsgcHJvdmlkZTogJ1NwcmVhZHNoZWV0U2VsZWN0aW9uJywgdXNlVmFsdWU6IFNlbGVjdGlvbn07XG5leHBvcnQgY29uc3QgQ29udGV4dE1lbnVTZXJ2aWNlOiBWYWx1ZVByb3ZpZGVyID0geyBwcm92aWRlOiAnU3ByZWFkc2hlZXRDb250ZXh0TWVudScsIHVzZVZhbHVlOiBDb250ZXh0TWVudX07XG5leHBvcnQgY29uc3QgRm9ybXVsYUJhclNlcnZpY2U6IFZhbHVlUHJvdmlkZXIgPSB7IHByb3ZpZGU6ICdTcHJlYWRzaGVldEZvcm11bGFCYXInLCB1c2VWYWx1ZTogRm9ybXVsYUJhcn07XG5leHBvcnQgY29uc3QgUmliYm9uU2VydmljZTogVmFsdWVQcm92aWRlciA9IHsgcHJvdmlkZTogJ1NwcmVhZHNoZWV0UmliYm9uJywgdXNlVmFsdWU6IFJpYmJvbn07XG5leHBvcnQgY29uc3QgU2F2ZVNlcnZpY2U6IFZhbHVlUHJvdmlkZXIgPSB7IHByb3ZpZGU6ICdTcHJlYWRzaGVldFNhdmUnLCB1c2VWYWx1ZTogU2F2ZX07XG5leHBvcnQgY29uc3QgT3BlblNlcnZpY2U6IFZhbHVlUHJvdmlkZXIgPSB7IHByb3ZpZGU6ICdTcHJlYWRzaGVldE9wZW4nLCB1c2VWYWx1ZTogT3Blbn07XG5leHBvcnQgY29uc3QgU2hlZXRUYWJzU2VydmljZTogVmFsdWVQcm92aWRlciA9IHsgcHJvdmlkZTogJ1NwcmVhZHNoZWV0U2hlZXRUYWJzJywgdXNlVmFsdWU6IFNoZWV0VGFic307XG5leHBvcnQgY29uc3QgRGF0YUJpbmRTZXJ2aWNlOiBWYWx1ZVByb3ZpZGVyID0geyBwcm92aWRlOiAnU3ByZWFkc2hlZXREYXRhQmluZCcsIHVzZVZhbHVlOiBEYXRhQmluZH07XG5leHBvcnQgY29uc3QgQ2VsbEZvcm1hdFNlcnZpY2U6IFZhbHVlUHJvdmlkZXIgPSB7IHByb3ZpZGU6ICdTcHJlYWRzaGVldENlbGxGb3JtYXQnLCB1c2VWYWx1ZTogQ2VsbEZvcm1hdH07XG5leHBvcnQgY29uc3QgTnVtYmVyRm9ybWF0U2VydmljZTogVmFsdWVQcm92aWRlciA9IHsgcHJvdmlkZTogJ1NwcmVhZHNoZWV0TnVtYmVyRm9ybWF0JywgdXNlVmFsdWU6IE51bWJlckZvcm1hdH07XG5leHBvcnQgY29uc3QgRm9ybXVsYVNlcnZpY2U6IFZhbHVlUHJvdmlkZXIgPSB7IHByb3ZpZGU6ICdTcHJlYWRzaGVldEZvcm11bGEnLCB1c2VWYWx1ZTogRm9ybXVsYX07XG5cbi8qKlxuICogTmdNb2R1bGUgZGVmaW5pdGlvbiBmb3IgdGhlIFNwcmVhZHNoZWV0IGNvbXBvbmVudCB3aXRoIHByb3ZpZGVycy5cbiAqL1xuQE5nTW9kdWxlKHtcbiAgICBpbXBvcnRzOiBbQ29tbW9uTW9kdWxlLCBTcHJlYWRzaGVldE1vZHVsZV0sXG4gICAgZXhwb3J0czogW1xuICAgICAgICBTcHJlYWRzaGVldE1vZHVsZVxuICAgIF0sXG4gICAgcHJvdmlkZXJzOltcbiAgICAgICAgQ2xpcGJvYXJkU2VydmljZSxcbiAgICAgICAgRWRpdFNlcnZpY2UsXG4gICAgICAgIEtleWJvYXJkTmF2aWdhdGlvblNlcnZpY2UsXG4gICAgICAgIEtleWJvYXJkU2hvcnRjdXRTZXJ2aWNlLFxuICAgICAgICBTZWxlY3Rpb25TZXJ2aWNlLFxuICAgICAgICBDb250ZXh0TWVudVNlcnZpY2UsXG4gICAgICAgIEZvcm11bGFCYXJTZXJ2aWNlLFxuICAgICAgICBSaWJib25TZXJ2aWNlLFxuICAgICAgICBTYXZlU2VydmljZSxcbiAgICAgICAgT3BlblNlcnZpY2UsXG4gICAgICAgIFNoZWV0VGFic1NlcnZpY2UsXG4gICAgICAgIERhdGFCaW5kU2VydmljZSxcbiAgICAgICAgQ2VsbEZvcm1hdFNlcnZpY2UsXG4gICAgICAgIE51bWJlckZvcm1hdFNlcnZpY2UsXG4gICAgICAgIEZvcm11bGFTZXJ2aWNlXG4gICAgXVxufSlcbmV4cG9ydCBjbGFzcyBTcHJlYWRzaGVldEFsbE1vZHVsZSB7IH0iXX0=