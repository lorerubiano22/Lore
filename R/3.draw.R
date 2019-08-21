outDir <- c("/home/laura/workspace/Lore/Experiments/Exp2/")
inDir <- c("/home/laura/workspace/Lore/inputs/")
# outDir <- c("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//outputs//Exp2")
# inDir <- c("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//inputs")
library(stringr)

draw <- function(instance, seed, var, nSim, numSol){
  setwd(inDir)
  nodesFile <- paste( instance, "_nodes.txt", sep = "", collapse = "")
  inputs <- read.table(nodesFile)
  colnames(inputs) <- c("x", "y", "dda")
  plot(inputs$x, inputs$y, xlab = "", ylab = "")
  points(inputs$x[1], inputs$y[1], pch = 15)
  
  setwd(outDir)
  listFiles <- list.files(".")
  listFiles <- listFiles[grep(instance, listFiles)]
  listFiles <- listFiles[grep(seed, listFiles)]
  solFile <- listFiles[grep(paste(nSim, var, sep = "_"), listFiles)]
  aux <- readChar(solFile, file.info(solFile)$size)
  aux <- strsplit(aux, "BSS")[[1]][numSol]
  routes <- NULL
  i1 <- str_locate_all(aux, "Route")[[1]]
  for(t in 1:(dim(i1)[1])){
    if(t < dim(i1)[1]){
      auxtxt <- substr(aux, i1[t,2] + 4, i1[t+1,1] - 3)
    }else{
      auxtxt <- substr(aux, i1[t,2] + 4, nchar(aux))}
    i11 <- str_locate_all(auxtxt, "0-")[[1]]
    i12 <- str_locate_all(auxtxt, "-0")[[1]]
    auxtxt <- substr(auxtxt, i11[1,1],  i12[1,2])
    auxtxt <- as.numeric(unlist(strsplit(auxtxt, "-")))
    routes[[t]] <- auxtxt}
  for(z in 1:length(routes)){
    cap <- 0
    accX <- 0
    accY <- 0
    for(k in 1:length(routes[[z]]) - 1){
      cap <- cap + as.numeric(inputs$dda[routes[[z]][k+1]+1])
      lines(x = inputs$x[routes[[z]][c(k,k+1)]+1], 
            y = inputs$y[routes[[z]][c(k,k+1)]+1])
      accX <- accX + as.numeric(inputs$x[routes[[z]][k+1]+1])
      accY <- accY + as.numeric(inputs$y[routes[[z]][k+1]+1])
    }
    accX <- accX/(length(routes[[z]]))
    accY <- accY/(length(routes[[z]]))
    text(x = accX, y = accY, labels=cap)
  }
}

instance <- "A-n33-k6"
var <- "0.1"
nSim <- "3000"
seed <- "25000"

par(mfrow = c(1,2))
draw(instance, seed, var, nSim, 1)
# x11()
text(43,95,"Total costs = 584.89")
text(43,89,"Exp total costs = 611.12")
text(43,83,"R = 0.81")
draw(instance, seed, var, nSim, 2)
text(43,95,"Total costs = 601.61")
text(43,89,"Exp total costs = 609.51")
text(43,83,"R = 0.87")