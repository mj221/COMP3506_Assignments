
from priority_queue import AdaptableHeapPriorityQueue
from airport_base import AirportBase


class Airport(AirportBase):

    """
        Implement all the necessary methods of the Airport here
    """
    def __init__(self, capacity):
        super().__init__(capacity)
        self.nodes = []
        self.edges = []
        self.time = {}
        self.visited =[]

    class Shuttle(AirportBase.ShuttleBase):
        """
            Implement all the necessary methods of the Shuttle here
        """
    class Terminal(AirportBase.TerminalBase):
        """
            Implement all the necessary methods of the Terminal here
        """

    def insert_terminal(self, terminal):
        self.nodes.append(terminal)
        return terminal
    
    def insert_shuttle(self, origin, destination, time):
        newShuttle = Airport.Shuttle(origin, destination, time)
        self.edges.append(newShuttle)
        return newShuttle

    def opposite(self, shuttle, terminal):
        if(shuttle.source == terminal):
            return shuttle.destination
        if (shuttle.destination == terminal):
            return shuttle.source
        return None

    def outgoing_shuttles(self, terminal):
        List = []
        for edge in self.edges:
            if(edge.source == terminal or edge.destination == terminal):
                List.append(edge)
        return List
    
    def remove_shuttle(self, shuttle):
        if shuttle not in self.edges:
            return False
        for edge in self.edges:
            if(edge == shuttle):
                self.edges.remove(edge)
                return True
    
    def remove_terminal(self, terminal):
        if terminal not in self.nodes:
            return False
        for node in self.nodes:
            if(node == terminal):
                self.nodes.remove(node)
                neighbours = self.outgoing_shuttles(node)
                for neighbour in neighbours:
                    self.remove_shuttle(neighbour)
                return True

    def find_shortest_path(self, origin, destination):
        PQ = AdaptableHeapPriorityQueue()
        for v in self.nodes:
            if (v == origin):
                self.time[v] = 0
            else:
                self.time[v] = float('inf')
            PQ.add(self.time[v], v)
        while(not PQ.is_empty()):
            (t, u) = PQ.remove_min()
            
            for e in self.outgoing_shuttles(u):
                z = self.opposite(e, u)
                r = self.time[u] + e.time
                # locator = PQ.add(z, z)
                if r < self.time[z]:
                    self.time[z] = r
                    PQ.update(PQ.add(self.time[z], z), r, z) 

        return self.time

    def find_fastest_path(self, origin, destination):
        return super().find_fastest_path(origin, destination)

if __name__ == "__main__":

    """
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        REMOVE THE MAIN FUNCTION BEFORE SUBMITTING TO THE AUTOGRADER 
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        The following main function is provided for simple debugging only
    """

    g = Airport(capacity=3)
    terminalA = g.insert_terminal(g.Terminal("A", 1))
    terminalB = g.insert_terminal(g.Terminal("B", 3))
    terminalC = g.insert_terminal(g.Terminal("C", 4))
    terminalD = g.insert_terminal(g.Terminal("D", 2))

    shuttle1 = g.insert_shuttle(terminalA, terminalB, 2)
    shuttle2 = g.insert_shuttle(terminalA, terminalC, 5)
    shuttle3 = g.insert_shuttle(terminalA, terminalD, 18)
    shuttle4 = g.insert_shuttle(terminalB, terminalD, 8)
    shuttle5 = g.insert_shuttle(terminalC, terminalD, 15)

    for i in g.nodes:
        print(i)
    print("----------")
    for i in g.edges:
        print(i)
    print("----------")
    time= g.find_shortest_path(terminalA, terminalD)
    
    
    # print(time[terminalB])

    # pq = AdaptableHeapPriorityQueue()
    # pq.add(terminalA.waiting_time, terminalA.id)
    # test1 = pq.add(terminalB.waiting_time, terminalB.id)
    # test = pq.add(terminalC.waiting_time, terminalC.id)
    # # pq.update(test1, 'Test', '760')
    # while (not pq.is_empty()):

    #     print(pq.remove_min())
  
##
##    # Opposite
##    assert g.opposite(shuttle1, terminalA).id == 'B'
##
##    # Outgoing Shuttles
##    assert [s.time for s in g.outgoing_shuttles(terminalA)] == [2, 5, 18]
##
##    # Remove Terminal
##    g.remove_terminal(terminalC)
##    assert [s.time for s in g.outgoing_shuttles(terminalA)] == [2, 18]
##
##    # Shortest path
##    shortest_path, distance = g.find_shortest_path(terminalA, terminalD)
##    assert [t.id for t in shortest_path] == ['A', 'D']
##    assert distance == 19
##
##    # Fastest path
##    fastest_path, distance = g.find_fastest_path(terminalA, terminalD)
##    assert [t.id for t in fastest_path] == ['A', 'B', 'D']
##    assert distance == 14
