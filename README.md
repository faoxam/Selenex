SELEN-EX
=========
What is selenex?
Selenex is a framework re-write from Selenium Web Driver to automate the Web application testing by executing the scenario driven
Selenex read driven scenario written in excel, process and generate the test result in excel

Capability?
Able to process multiple excel files

Why selenex?
No need any programming knowledge. User just need to provide scenario in excel with minimum programming knowledge

What tecnical knowledge required?
User just need to know the Inspect Element to grap the element to verify. Advance user should be able to use the annotation provided in
selenex for complex scenarios.





---------------------------------------------------------------------------------
0. [[$config$]]
This will be use to specified the location of browser type, browser driver path, browser profiles
Support execute concurrently for different profiles but limited to single type of browser.

[[Available component in $config$ ]]
#Driver properties             [This indicate the browser configuration]
browser                  [Browser type, Firefox, IE, Safari, Chrome or etc ]
driver_path                 [Selenium browser Driver location]        
profile_path                 [Profile path indicate different browser configurtaion]
You may able to specified the number of different profiles to be execute simultaneously using "&" referencial
Syntax "&<SheetName>.<ColumnHeader>"
 
-----------------------------------------------------

1. [[Available Selector Type]]
1) xpath                 [XPATH string - Recommended because it was unique value for each element]
2) id                        [Element ID - Only the ID unique in the page]
3) cssselector          [CSS path string - Also recommended as it was unique for each element, however a bit complex compare to XPATH]
4) name                        [Element name - Only the name unique in the page]
5) tagname              [Element tagname, eg. select, span, anchor or etc - Only the tagname unique in the page]
6) partiallinktext         [Use for href]
7) linktext                   [Use for href]
8) classname                   [Element class- Only the tagname unique in the page]


2. [[Scenario Header]]
Action           - Refer item 3. [[Supported Action]] for for all supported keyword        
Selector Type        - Refer item 1. [[Available Selector Type]] for all suppported keyword
Selector String - This is the string value indicated the element. The format depend on th value of Selector Type
Value                - Input value or expected result
Note                - This will appear in Excel result


--------------------------------------------------------
3. [[Supported Action]]
Refer "action.cfg" for all supported action keyword

[Keyword: browse]
This to automate the user to browse to given URL in selector string
Selector Type must be "URL"
The URL need to set in Selector String column

[Keyword: input]
This to automate the user input.

Main
By default the text in Value column will be use as input (Single input)
Features: Support a set of input
Value --> Support "&" indicate the referral of TAB to be use for Different input
Syntax: "&<SheetName>.<ColumnHeader>"

Child
<SheetName> must consist <ColumnHeader> as header and Input value in the below of the <Colum-Name> header.

[Keyword: click]
Click on the given element stated at Selector String column

[Keyword: hover]
Automate Hovering
Use <Alt> + <Enter> at Value column to add more than 1 value to hover in sequence

[Keyword: hover-and-click]
Automate Hovering and and click (in sequence)
Use <Alt> + <Enter> at Value column to add another XPAT
The


[Keyword: validate-text]
Validate single text element in a page
Support regex pattern for expected text
 

[Keyword: validate-texts]
Validate a list of texts for multiple element in a page
Support regex pattern for expected text

Main
Only Selector type = Only "XPATH" will be supported.
Selector String --> Need to be set as a parent of child elements. Can be leave it blank if the child contain full XPATH
Value           --> "@texts.<SheetName>" - Indicate the <SheetName> to refer "Child texts format"


Child
"Child texts format" must consist the below column Header
        1) Child  - The extended XPATH of child element to validate ** Currently only support XPATH **        
        2) Regex  - Expected text to be display in the page        
        3) Remark - This remark will be shown in EXCEL result





[Keyword: validate-column]
This will be use to validate the Table contents

Main
Selector String - Suggest to put the find the element with <tbody> tagName
Value - Using "@table" annotation. Syntax @table.<SheetName>

Child (<SheetName>)
Key         - Use "@Column-<Column-Index>" to indicate position the table-column field to validate
Regex        - Expected result
Remark        - This will be shown in Excel Result




[Keyword: validate-cascade]
This to validate the repeatable Boxes in the pages

Main
Selector String - Suggest to put the top parent element of the boxes to validate
Value - Using "@cascade" annotation. Syntax @cascade.<SheetName>

Child (<SheetName>)
Parent Tag        - Box element tagName
Child                - XPATH of the child location after the parent tag        
Regex                - Expected result
Remark                - This will be shown in Excel Result

[Keyword: is-exist]
Ensure the value is exist on specified element
    Accept annotation in scenario.value column as below
    "@value." - By value in component
    "@text."  - By visible text in component
    Default (No annotation specified) will use By visible text

[Keyword: is-not-exist]
Ensure the value does not exist on specified element
    Accept annotation in scenario.value column as below
    "@value." - By value in component
    "@text."  - By visible text in component
    Default (No annotation specified) will use By visible text

[Keyword: select]
    Automate the SELECT (Single) from Drop-Down or Combo-Box list
    The value specified in value separated by "new line" in excel box
    Accept annotation in scenario.value column as below
    "@value." - By value in component
    "@text." - By visible text in component
    "@index." - By index count in component
    Default (No annotation specified) will use By visible text

[Keyword: selects]
    Automate the SELECT (Multiple) from Drop-Down or Combo-Box list
    The value specified in value separated by "new line" in excel box
    Accept annotation in scenario.value column as below
    "@value." - By value in component
    "@text." - By visible text in component
    "@index." - By index count in component
    Default (No annotation specified) will use By visible text

[Keyword: is-select]
    Validate the value (Single) of selected in Drop-Down or Combo-Box
    Accept annotation in scenario.value column as below
    "@value."          - By value in component
    "@text."        - By visible text in component
    "@index."        - By index count in component
    Default (No annotation specified) will use By visible text

[Keyword: is-selects]
    Validate the value (Multiple) of selected in Drop-Down or Combo-Box
    Use <Alt> + <Enter> at Value column to add more than 1 value to validate
    The value specified in value separated by "new line" in excel box
    Accept annotation in scenario.value column as below
    "@value."          - By value in component
    "@text."        - By visible text in component
    "@index."        - By index count in component
    Default (No annotation specified) will use By visible text

[Keyword: is-not-select]
    Ensure the specified value NOT being selected from Drop-Down or Combo-Box list
    Accept annotation in scenario.value column as below
    "@value." - By value in component
    "@text." - By visible text in component
    "@index." - By index count in component
    Default (No annotation specified) will use By visible text

[Keyword: is-not-selects]
    Ensure NONE of specified value NOT being selected from Drop-Down or Combo-Box list
    The value specified in value separated by "new line" in excel box
    Accept annotation in scenario.value column as below
    "@value." - By value in component
    "@text." - By visible text in component
    "@index." - By index count in component
    Default (No annotation specified) will use By visible text

[Keyword: select-radio]
    Automate the SELECT from radio button group
    Accept annotation in scenario.value column as below
    "@value." - By.value locator
    "@text." - By.linkText locator
    "@index." - By.index locator
    "@xpath." - By.xpath locator
    "@name." - By.name locator
    "@id." - By.id locator
    "@css." - By.css locator
    Default (No annotation specified) will use By.linkText

[Keyword: is-radio-selected]
    Validate the give value from radio button group is NOT selected
    Accept annotation in scenario.value column as below
    "@value." - By.value locator
    "@text." - By.linkText locator
    "@index." - By.index locator
    "@xpath." - By.xpath locator
    "@name." - By.name locator
    "@id." - By.id locator
    "@css." - By.css locator
    Default (No annotation specified) will use By.linkText

[Keyword: is-radio-not-selected]
    Ensure the specified value in from radio button group
    Accept annotation in scenario.value column as below
    "@value." - By.value locator
    "@text." - By.linkText locator
    "@index." - By.index locator
    "@xpath." - By.xpath locator
    "@name." - By.name locator
    "@id." - By.id locator
    "@css." - By.css locator
    Default (No annotation specified) will use By.linkText
