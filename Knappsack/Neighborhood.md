### Neighborhood search for the knapsack problem
```
function Neighborhood(problem) returns a solution
    solution <- GREEDY-ALGORITHM(problem)
    knappsacks <- m knapsacks with capacity W
    items <- n items with value p and weight w 
    state <- packlist, description of a items packed in a knapsack
    while moveditem
        solution,moveditem <- ROTATE_ITEM(solution)
        items <-UPDATE-STATE(state, knapsacks, items)
        if items not empty 
            for each knapsack in random order do
                if knapsack kan hold item then
                    knapsack <- add
                    exit for            
    solution <- GREEDY-ALGORITHM(solution)
    return  
```
****
```
function ROTATE-ITEM(solution,items,knappsacks,state) returns solution and moveditem
    knappsack <- select knappsack with most space left
    spaceleft <- space left in selected knappsack
    _items <- items in selected knappsack
    moveditem <- false
    for each _item in _items
        for item in items
            if _itemweight + spaceleft equals itemweight
                remove _item
                add item 
                moveditem <- true
                break           
```



