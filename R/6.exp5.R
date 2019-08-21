# Experiment 5: time & seeds
library("stringr")
install.packages("scatterplot3d")
library("scatterplot3d")
setwd("/home/laura/workspace/Lore/outputs/Exp5/")
# setwd("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//outputs//Exp5//")
source("/home/laura/workspace/Lore/R/0.3reading.R")
# source("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//R//0.3reading.R")
info <- c("Instance", "MaxTime", "BSS-exp total c", "BSS-rel")

mat1 <- mat[,c(1,14,45,53,57)]
for(j in c(2:4)){
  mat1[,j] <- as.numeric(as.character(mat1[,j]))}
sum(mat1[,5]=="false")
mat1 <- mat1[,-c(5)]
auxplot <- mat1
auxplot1 <- subset(auxplot, auxplot[,1]==levels(auxplot$Instance)[1])
auxplot1 <- auxplot1[order(auxplot1$pTime),]
auxplot2 <- subset(auxplot, auxplot[,1]==levels(auxplot$Instance)[2])
auxplot2 <- auxplot2[order(auxplot2$pTime),]
auxplot1 <- cbind(auxplot1, seed=rep(1:10,7))
auxplot2 <- cbind(auxplot2, seed=rep(1:10,7))

auxplot11 <- auxplot1
  
for(i in 1:7){
  for(j in 1:10){    
    a <- which.min(auxplot1[c(((i-1)*10+1):((i-1)*10+j)),3])
    b <- (i-1)*10+a
    auxplot11[((i-1)*10+j),c(3:4)] <- auxplot1[b,(3:4)]    
  }
}

#par(mfrow=c(1,2))
scatterplot3d(auxplot11$pTime, auxplot11$seed, auxplot11$`BSS-exp total c`, pch=16, highlight.3d=TRUE, type="h",
              xlab="Seconds", ylab="Number of seeds", zlab="Exp total cost")
#scatterplot3d(auxplot11$pTime, auxplot11$seed, auxplot11$`BSS-rel`, pch=16, highlight.3d=TRUE, type="h",
 #             xlab="Seconds", ylab="Number of seeds", zlab="Reliability")