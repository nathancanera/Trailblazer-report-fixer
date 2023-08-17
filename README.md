# Trailblazer report fixer
When I worked at Panda one of my tasks was to pull a crime report from trailblazer for all 2400 stores. They used a website called trailblazer which could pull reports one by one. I created a whole new process for getting those reports and this is one part of it. 

Part of the issue with trailblazer is that once you pull the reports it names the files as the address of the store but more than half the time the address is wrong some way. It could be typos, wrong street number, wrong street, missing information, etc...
I created a organization and sorting algorithm that uses levenshtein string similarity to organize each report into a map of maps that goes <State, <city, <zip code, <street address, store number>>>>. My algorithm is able to handle Trailblazer's inconsistent formatting and it organizes each file into that format and then compares it to addresses we have on file to find the correct store number and rename the file appropriately. 
